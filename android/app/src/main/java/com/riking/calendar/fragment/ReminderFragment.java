package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ldf.calendar.Utils;
import com.riking.calendar.R;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.ReminderAdapter;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.util.CONST;
import com.riking.calendar.view.CustomLinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.Sort;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class ReminderFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView tomorrowRecyclerView;
    Realm realm;
    ViewPagerActivity a;
    CustomLinearLayout root;
    View checkHistoryButton;
    TextView today;
    View todayTitle;
    TextView tomorrow;
    TextView tomorrowTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_fragment, container, false);
        a = (ViewPagerActivity) getActivity();
        root = (CustomLinearLayout) v.findViewById(R.id.custom_linear_layout);
        checkHistoryButton = v.findViewById(R.id.check_task_history);
        today = (TextView) v.findViewById(R.id.today_date);
        todayTitle = v.findViewById(R.id.today_title);
        tomorrow = (TextView) v.findViewById(R.id.tomorrow_date);
        tomorrowTitle = (TextView) v.findViewById(R.id.tomorrow_title);

        setRecyclerView(v);
        root.onDraggingListener = new CustomLinearLayout.OnDraggingListener() {
            @Override
            public void scrollUp() {
                if (checkHistoryButton.getVisibility() == View.VISIBLE) {
                    checkHistoryButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void scrollDown() {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                Log.d("zzw", "scroll down" + " first item visibility: " + viewHolder.itemView.getVisibility());
//                if (viewHolder == null || (viewHolder.itemView.getVisibility() == View.VISIBLE && (checkHistoryButton.getVisibility() == View.GONE || checkHistoryButton.getVisibility() == View.INVISIBLE))) {
                checkHistoryButton.setVisibility(View.VISIBLE);
//                }
            }
        };
        return v;
    }

    private void setRecyclerView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(a));
        tomorrowRecyclerView = (RecyclerView) v.findViewById(R.id.tomorrow_recycler_view);
        tomorrowRecyclerView.setLayoutManager(new LinearLayoutManager(a));

        realm = Realm.getDefaultInstance();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == Calendar.SUNDAY) {
            weekDay = 7;
        } else {
            weekDay--;
        }

        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
        List<Reminder> reminders = realm.where(Reminder.class).beginGroup().equalTo("day", dayFormat.format(date)).equalTo("repeatFlag", 0).endGroup()
                .or().beginGroup()
                .equalTo("repeatFlag", CONST.REPEAT_FLAG_WEEK)
                .contains("repeatWeek", String.valueOf(weekDay))
                .endGroup()
                .findAllSorted("time", Sort.ASCENDING);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayout.VERTICAL));
        recyclerView.setAdapter(new ReminderAdapter(reminders));
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                //the data is changed.
                recyclerView.getAdapter().notifyDataSetChanged();
                tomorrowRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        if (reminders.size() > 0) {
            todayTitle.setVisibility(View.VISIBLE);
            today.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
            String message = sdf.format(date) + " " + Utils.getWeekdayPosition(date);
            today.setText(message);
        } else {
            today.setVisibility(View.GONE);
            todayTitle.setVisibility(View.GONE);
        }

        //tomorrow
        c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);

        weekDay++;
        if (weekDay > 7) {
            weekDay = 1;
        }
        //set tomorrow
        List<Reminder> tomorrowReminders = realm.where(Reminder.class).beginGroup().equalTo("day", dayFormat.format(c.getTime())).equalTo("repeatFlag", 0).endGroup()
                .or().beginGroup()
                .equalTo("repeatFlag", CONST.REPEAT_FLAG_WEEK)
                .contains("repeatWeek", String.valueOf(weekDay))
                .endGroup()
                .findAllSorted("time", Sort.ASCENDING);
        tomorrowRecyclerView.setAdapter(new ReminderAdapter(tomorrowReminders));

        if (reminders.size() > 0) {
            tomorrowTitle.setVisibility(View.VISIBLE);
            tomorrow.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
            String message = sdf.format(date) + " " + Utils.getWeekdayPosition(date);
            tomorrow.setText(message);
        } else {
            tomorrow.setVisibility(View.GONE);
            tomorrowTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}
