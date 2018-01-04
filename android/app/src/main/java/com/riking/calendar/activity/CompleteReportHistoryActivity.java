package com.riking.calendar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.base.ZActivity;
import com.riking.calendar.adapter.CompletedReportHistoryAdapter;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.interfeet.AdapterEmptyListener;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.viewholder.base.ZViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class CompleteReportHistoryActivity extends ZActivity<CompleteReportHistoryActivity.CompleteReportHistoryAdapter> {

    @Override
    public CompleteReportHistoryAdapter getAdapter() {
        return new CompleteReportHistoryAdapter();
    }

    @Override
    public void loadData(int page) {
        mPullToLoadView.setComplete();
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initEvents() {
    }

    public void clickBack(View view) {
        onBackPressed();
    }

    public class CompleteReportHistoryAdapter extends ZAdater<MyViewHolder,List<Task>> {
        ArrayList<List<Task>> daysWithTasks;
        ArrayList<String> titles;

        public CompleteReportHistoryAdapter() {
        }

        public CompleteReportHistoryAdapter(LinkedHashMap<String, List<Task>> tasks) {
            this.daysWithTasks = new ArrayList<>(tasks.values());
            titles = new ArrayList<>(tasks.keySet());
        }

    /*    @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.finished_task_row, parent, false);
            return new MyViewHolder(itemView);
        }*/
/*

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Log.d("zzw", "position: " + position + " key " + titles.get(position));
            holder.completedDate.setText("完成于" + titles.get(position));
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.completedDate.getContext()));
            holder.recyclerView.setAdapter(new CompletedReportHistoryAdapter(daysWithTasks.get(position)));
        }
*/

        @Override
        public void onBindVH(MyViewHolder holder, int position) {
            Log.d("zzw", "position: " + position + " key " + titles.get(position));
            holder.completedDate.setText("完成于" + titles.get(position));
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.completedDate.getContext()));
            holder.recyclerView.setAdapter(new CompletedReportHistoryAdapter(daysWithTasks.get(position)));
        }

        @Override
        public MyViewHolder onCreateVH(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.finished_task_row, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    public class MyViewHolder extends ZViewHolder {
        public TextView completedDate;
        public RecyclerView recyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            completedDate = (TextView) itemView.findViewById(R.id.completed_day);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.nested_recyclerview);
        }
    }
}
