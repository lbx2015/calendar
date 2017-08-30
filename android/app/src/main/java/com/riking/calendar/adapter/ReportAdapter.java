package com.riking.calendar.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.realm.model.QueryReportContainerRealmModel;
import com.riking.calendar.realm.model.QueryReportRealmModel;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    RealmResults<QueryReportContainerRealmModel> reports;

    public ReportAdapter(RealmResults<QueryReportContainerRealmModel> reports) {
        this.reports = reports;
    }

    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.primary_report_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        QueryReportContainerRealmModel reportContainer = reports.get(position);
        Log.d("zzw", "position: " + position + " key " + reportContainer.title);
        holder.reportName.setText(reportContainer.title);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.reportName.getContext()));
        holder.recyclerView.setAdapter(new ReportItemAdapter(reportContainer.result));
        holder.reportName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.recyclerView.getVisibility() == View.VISIBLE) {
                    holder.recyclerView.setVisibility(View.GONE);
                    holder.firstDivider.setVisibility(View.GONE);
                    holder.arrowImage.setRotation(90);
                } else {
                    holder.arrowImage.setRotation(0);
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    holder.firstDivider.setVisibility(View.VISIBLE);
                }
            }
        });

        if (position + 1 == reports.size()) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView reportName;
        public RecyclerView recyclerView;
        public View divider;
        public View firstDivider;
        public ImageView arrowImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            reportName = (TextView) itemView.findViewById(R.id.report_name);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.nested_recyclerview);
            divider = itemView.findViewById(R.id.divider);
            firstDivider = itemView.findViewById(R.id.first_divider);
            arrowImage = (ImageView) itemView.findViewById(R.id.arrow_button);
        }
    }
}