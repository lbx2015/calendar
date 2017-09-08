package com.riking.calendar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldf.calendar.Const;
import com.ldf.calendar.Utils;
import com.riking.calendar.R;
import com.riking.calendar.activity.RemindHistoryActivity;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.ReminderAdapter;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;
import com.riking.calendar.view.CustomLinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class ReminderFragment extends Fragment {
    public Realm realm;
    protected SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RecyclerView tomorrowRecyclerView;
    RecyclerView futureRecyclerView;
    ViewPagerActivity a;
    CustomLinearLayout root;
    NestedScrollView nestedScrollView;
    View checkHistoryButton;
    TextView today;
    View todayTitle;
    TextView tomorrow;
    TextView tomorrowTitle;
    TextView futureTitle;
    CustomLinearLayout emptyView;
    View remindersRoot;
    View v;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        v = inflater.inflate(R.layout.reminder_fragment, container, false);
        a = (ViewPagerActivity) getActivity();
        remindersRoot = v.findViewById(R.id.reminds);
        root = (CustomLinearLayout) v.findViewById(R.id.custom_linear_layout);
        emptyView = (CustomLinearLayout) v.findViewById(R.id.empty);
        checkHistoryButton = v.findViewById(R.id.check_remind_history);
        today = (TextView) v.findViewById(R.id.today_date);
        todayTitle = v.findViewById(R.id.today_title);
        tomorrow = (TextView) v.findViewById(R.id.tomorrow_date);
        tomorrowTitle = (TextView) v.findViewById(R.id.tomorrow_title);
        futureTitle = (TextView) v.findViewById(R.id.future_title);
        nestedScrollView = (NestedScrollView) v.findViewById(R.id.nested_recyclerview);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkHistoryButton.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
                if (Preference.pref.getBoolean(Const.IS_LOGIN, false)) {
                    //get reminders and tasks of user from server
                    APIClient.synchAll();
                }
            }
        });


        setRecyclerView(v);
        emptyView.onDraggingListener = new CustomLinearLayout.OnDraggingListener() {
            @Override
            public void scrollUp() {
                if (checkHistoryButton.getVisibility() == View.VISIBLE) {
                    checkHistoryButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void scrollDown() {
                checkHistoryButton.setVisibility(View.VISIBLE);
            }
        };
        root.onDraggingListener = new CustomLinearLayout.OnDraggingListener() {
            @Override
            public void scrollUp() {
                if (checkHistoryButton.getVisibility() == View.VISIBLE) {
                    checkHistoryButton.setVisibility(View.GONE);
                }
//                a.bottomTabs.setVisibility(View.GONE);
            }

            @Override
            public void scrollDown() {
//                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
//                if (viewHolder == null || (viewHolder.itemView.getVisibility() == View.VISIBLE && (checkHistoryButton.getVisibility() == View.GONE || checkHistoryButton.getVisibility() == View.INVISIBLE))) {
//                checkHistoryButton.setVisibility(View.VISIBLE);
//                }
//                a.bottomTabs.setVisibility(View.VISIBLE);
            }
        };

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final int marginBottom = a.bottomTabs.getMeasuredHeight();
        params.setMargins(0, 0, 0, marginBottom);
        root.setLayoutParams(params);

        checkHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the remind history page
                startActivity(new Intent(getContext(), RemindHistoryActivity.class));
            }
        });
        return v;
    }

    private void setRecyclerView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(a));
        tomorrowRecyclerView = (RecyclerView) v.findViewById(R.id.tomorrow_recycler_view);
        tomorrowRecyclerView.setLayoutManager(new LinearLayoutManager(a));
        futureRecyclerView = (RecyclerView) v.findViewById(R.id.future_recycler_view);
        futureRecyclerView.setLayoutManager(new LinearLayoutManager(a));

        final Date date = new Date();
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        //tomorrow
        c.set(Calendar.DATE, c.get(Calendar.DATE) + 1);

        int weekDay = c.get(Calendar.DAY_OF_WEEK);
        if (weekDay == Calendar.SUNDAY) {
            weekDay = 7;
        } else {
            weekDay--;
        }

        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
        RealmResults<Reminder> reminders = realm.where(Reminder.class).notEqualTo("deleteState", 1).beginGroup().equalTo("day", dayFormat.format(date)).equalTo("repeatFlag", 0).endGroup()
                .or().beginGroup()
                .equalTo("repeatFlag", CONST.REPEAT_FLAG_WEEK)
                .contains("repeatWeek", String.valueOf(weekDay))
                .endGroup()
                .findAllSorted("time", Sort.ASCENDING);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayout.VERTICAL));
        recyclerView.setAdapter(new ReminderAdapter(reminders, realm));

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                //the data is changed.
                RecyclerView.Adapter todayAdapter = recyclerView.getAdapter();
                todayAdapter.notifyDataSetChanged();
                RecyclerView.Adapter tomorrowAdapter = tomorrowRecyclerView.getAdapter();
                tomorrowAdapter.notifyDataSetChanged();
                RecyclerView.Adapter futureAdapter = futureRecyclerView.getAdapter();
                futureAdapter.notifyDataSetChanged();

                if (todayAdapter.getItemCount() > 0) {
                    todayTitle.setVisibility(View.VISIBLE);
                    today.setVisibility(View.VISIBLE);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
                    String message = sdf.format(date) + " " + Utils.getWeekdayPosition(date);
                    today.setText(message);
                } else {
                    today.setVisibility(View.GONE);
                    todayTitle.setVisibility(View.GONE);
                }

                if (tomorrowAdapter.getItemCount() > 0) {
                    tomorrowTitle.setVisibility(View.VISIBLE);
                    tomorrow.setVisibility(View.VISIBLE);
                    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
                    String message = sdf.format(c.getTime()) + " " + Utils.getWeekdayPosition(c.getTime());
                    tomorrow.setText(message);
                } else {
                    tomorrow.setVisibility(View.GONE);
                    tomorrowTitle.setVisibility(View.GONE);
                }

                if (futureAdapter.getItemCount() > 0) {
                    futureTitle.setVisibility(View.VISIBLE);
                } else {
                    futureTitle.setVisibility(View.GONE);
                }

                if (todayAdapter.getItemCount() == 0 && tomorrowAdapter.getItemCount() == 0 && futureAdapter.getItemCount() == 0) {
                    emptyView.setVisibility(View.VISIBLE);
                    remindersRoot.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.GONE);
                    remindersRoot.setVisibility(View.VISIBLE);
                }
            }
        });


        updateToday(reminders, date);

        weekDay++;
        if (weekDay > 7) {
            weekDay = 1;
        }
        //set tomorrow
        final List<Reminder> tomorrowReminders = realm.where(Reminder.class).beginGroup().equalTo("day", dayFormat.format(c.getTime())).equalTo("repeatFlag", 0).endGroup()
                .or().beginGroup()
                .equalTo("repeatFlag", CONST.REPEAT_FLAG_WEEK)
                .contains("repeatWeek", String.valueOf(weekDay))
                .endGroup().equalTo("deleteState", 0)
                .findAllSorted("time", Sort.ASCENDING);
        tomorrowRecyclerView.setAdapter(new ReminderAdapter(tomorrowReminders, realm));

        if (tomorrowReminders.size() > 0) {
            tomorrowTitle.setVisibility(View.VISIBLE);
            tomorrow.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
            String message = sdf.format(c.getTime()) + " " + Utils.getWeekdayPosition(c.getTime());
            tomorrow.setText(message);
        } else {
            tomorrow.setVisibility(View.GONE);
            tomorrowTitle.setVisibility(View.GONE);
        }

        //future reminders
        List<Reminder> futureReminders = realm.where(Reminder.class).beginGroup().equalTo("repeatFlag", 0)
                .greaterThan("reminderTime", c.getTime())
                .endGroup().or()
                .beginGroup()
                .notEqualTo("repeatFlag", CONST.NOT_REPEAT_FLAG_WEEK)
//                .contains("repeatWeek", String.valueOf(weekDay))
                .endGroup().equalTo("deleteState", 0)
                .findAllSorted("reminderTime", Sort.ASCENDING);
        futureRecyclerView.setAdapter(new ReminderAdapter(futureReminders, realm));
        if (futureReminders.size() > 0) {
            futureTitle.setVisibility(View.VISIBLE);
        } else {
            futureTitle.setVisibility(View.GONE);
        }

        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                recyclerView.getAdapter().notifyDataSetChanged();
                tomorrowRecyclerView.getAdapter().notifyDataSetChanged();
                futureRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        if (tomorrowReminders.size() == 0 && reminders.size() == 0 && futureReminders.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            remindersRoot.setVisibility(View.GONE);
//            emptyView.bringToFront();
        } else {
            emptyView.setVisibility(View.GONE);
            remindersRoot.setVisibility(View.VISIBLE);
        }
    }

    private void updateToday(RealmResults<Reminder> reminders, Date date) {
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}
