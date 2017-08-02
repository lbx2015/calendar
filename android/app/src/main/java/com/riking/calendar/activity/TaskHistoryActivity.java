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
import com.riking.calendar.adapter.CompletedTaskAdapter;
import com.riking.calendar.realm.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class TaskHistoryActivity extends AppCompatActivity {
    public Realm realm;
    private RecyclerView mPrimaryRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);
        mPrimaryRecyclerView = (RecyclerView) findViewById(R.id.primary_recycler_view);
        mPrimaryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        realm = Realm.getDefaultInstance();
        List<Task> tasks = realm.where(Task.class).equalTo("isDone", true).findAllSorted("completeDay", Sort.ASCENDING);

        LinkedHashMap<String, List<Task>> daysWithTasks = new LinkedHashMap<>();
        int size = tasks.size();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < size; i++) {
            Task t = tasks.get(i);
            String key = simpleDateFormat.format(t.completeDay).toLowerCase();
            Log.d("zzw", "date format: " + key);
            if (!daysWithTasks.containsKey(key)) {
                daysWithTasks.put(key, new ArrayList<Task>());
            }
            daysWithTasks.get(key).add(t);
        }

        mPrimaryRecyclerView.setAdapter(new TaskHistoryAdapter(daysWithTasks));
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public class TaskHistoryAdapter extends RecyclerView.Adapter<MyViewHolder> {
        ArrayList<List<Task>> daysWithTasks;
        ArrayList<String> titles;

        public TaskHistoryAdapter(LinkedHashMap<String, List<Task>> tasks) {
            this.daysWithTasks = new ArrayList<>(tasks.values());
            titles = new ArrayList<>(tasks.keySet());
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.finished_task_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Log.d("zzw", "position: " + position + " key " + titles.get(position));
            holder.completedDate.setText("完成于" + titles.get(position));
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.completedDate.getContext()));
            holder.recyclerView.setAdapter(new CompletedTaskAdapter(realm,daysWithTasks.get(position)));
        }

        @Override
        public int getItemCount() {
            Log.d("zzw", "daysWithTasks.size() : " + daysWithTasks.size());
            return daysWithTasks.size();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
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
