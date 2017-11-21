package com.riking.calendar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.pojo.QueryReport;

import java.util.ArrayList;
import java.util.List;


public class InterestingReportAdapter extends RecyclerView.Adapter<InterestingReportHolder> {

    public List<QueryReport> mList;
    private Context context;

    public InterestingReportAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public InterestingReportHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.intersting_report_item, viewGroup, false);
        return new InterestingReportHolder(view);
    }

    @Override
    public void onBindViewHolder(final InterestingReportHolder h, int i) {
        QueryReport c = mList.get(i);
        h.reportName.setText(c.reportName + " +");
        h.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.checked) {
                    //the report is not been subscribed
                    h.checked = false;
                    h.checkImage.setImageDrawable(h.checkImage.getResources().getDrawable(R.drawable.login_icon_add));
                    h.reportName.setTextColor(h.reportName.getResources().getColor(R.color.black_deep));
                    h.root.setBackground(h.root.getResources().getDrawable(R.drawable.not_selected__interesting_reports_background));
                } else {
                    //the report is been subscribed.
                    h.checked = true;
                    h.checkImage.setImageDrawable(h.checkImage.getResources().getDrawable(R.drawable.login_icon_dh));
                    h.reportName.setTextColor(h.reportName.getResources().getColor(R.color.white));
                    h.root.setBackground(h.root.getResources().getDrawable(R.drawable.selected__interesting_reports_background));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
