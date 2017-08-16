package com.riking.calendar.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.pojo.QueryReportContainer;
import com.riking.calendar.pojo.QueryReportModel;

import java.util.ArrayList;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    ArrayList<QueryReportContainer> reports;

    public ReportAdapter(QueryReportModel reportModel) {
        this.reports = reportModel._data;
    }

    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.primary_report_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        QueryReportContainer reportContainer = reports.get(position);
        Log.d("zzw", "position: " + position + " key " + reportContainer.title);
        holder.reportName.setText(reportContainer.title);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.reportName.getContext()));
        holder.recyclerView.setAdapter(new ReportItemAdapter(reportContainer.result));
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