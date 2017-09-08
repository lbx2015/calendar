package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.ReminderAdapter;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.view.ZRecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class RemindHistoryActivity extends AppCompatActivity {
    public Realm realm;
    private ZRecyclerView mPrimaryRecyclerView;
    private View emptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_history);
        mPrimaryRecyclerView = (ZRecyclerView) findViewById(R.id.primary_recycler_view);

        mPrimaryRecyclerView.emptyViewCallBack = new ZRecyclerView.EmptyViewCallBack() {
            @Override
            public void onEmpty() {
                Logger.d("zzw", "on Empty");
                emptyView.setVisibility(View.VISIBLE);
                mPrimaryRecyclerView.setVisibility(View.GONE);
            }

            @Override
            public void onNotEmpty() {
                emptyView.setVisibility(View.GONE);
                mPrimaryRecyclerView.setVisibility(View.VISIBLE);
            }
        };
        mPrimaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        emptyView = findViewById(R.id.empty);

        realm = Realm.getDefaultInstance();
        final Date date = new Date();
        RealmResults<Reminder> reminders = realm.where(Reminder.class).beginGroup().lessThan("reminderTime", date).equalTo("repeatFlag", 0).endGroup()
                .findAllSorted("time", Sort.ASCENDING);

        LinkedHashMap<String, List<Reminder>> daysWithTasks = new LinkedHashMap<>();
        int size = reminders.size();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        SimpleDateFormat yearDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        int currentYear = date.getYear();
        int currentDay = date.getDay();
        String key;
        for (int i = 0; i < size; i++) {
            Reminder t = reminders.get(i);

            if (t.reminderTime.getYear() != currentYear) {
                key = yearDateFormat.format(t.reminderTime).toLowerCase();
            } else if (currentDay - t.reminderTime.getDate() == 1) {
                key = "昨天";
            } else {
                key = simpleDateFormat.format(t.reminderTime).toLowerCase();
            }
            Log.d("zzw", "date format: " + key);
            if (!daysWithTasks.containsKey(key)) {
                daysWithTasks.put(key, new ArrayList<Reminder>());
            }
            daysWithTasks.get(key).add(t);
        }

        mPrimaryRecyclerView.setAdapter(new ReminderHistoryAdapter(daysWithTasks));
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public class ReminderHistoryAdapter extends RecyclerView.Adapter<MyViewHolder> {
        ArrayList<List<Reminder>> daysWithTasks;
        ArrayList<String> titles;

        public ReminderHistoryAdapter(LinkedHashMap<String, List<Reminder>> tasks) {
            this.daysWithTasks = new ArrayList<>(tasks.values());
            titles = new ArrayList<>(tasks.keySet());
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.finished_remind_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Log.d("zzw", "position: " + position + " key " + titles.get(position));
            holder.completedDate.setText("完成于" + titles.get(position));
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.completedDate.getContext()));
            holder.recyclerView.setAdapter(new ReminderAdapter(daysWithTasks.get(position), realm));
        }

        @Override
        public int getItemCount() {
            Log.d("zzw", "daysWithTasks.size() : " + daysWithTasks.size());
            return daysWithTasks.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView completedDate;
        public RecyclerView recyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            completedDate = (TextView) itemView.findViewById(R.id.completed_day);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.nested_recyclerview);
        }
    }
}
