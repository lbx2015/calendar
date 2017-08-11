package com.riking.calendar.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.pojo.Report;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    ArrayList<List<Report>> reports;
    ArrayList<String> titles;

    public ReportAdapter(LinkedHashMap<String, List<Report>> reports) {
        this.reports = new ArrayList<>(reports.values());
        titles = new ArrayList<>(reports.keySet());
    }

    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.primary_report_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.d("zzw", "position: " + position + " key " + titles.get(position));
        holder.reportName.setText(titles.get(position));
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.reportName.getContext()));
        holder.recyclerView.setAdapter(new ReportItemAdapter(reports.get(position)));
        holder.reportName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.recyclerView.getVisibility() == View.VISIBLE) {
                    holder.recyclerView.setVisibility(View.GONE);
                } else {
                    holder.recyclerView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reportName;
        public RecyclerView recyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            reportName = (TextView) itemView.findViewById(R.id.report_name);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.nested_recyclerview);
        }
    }
}