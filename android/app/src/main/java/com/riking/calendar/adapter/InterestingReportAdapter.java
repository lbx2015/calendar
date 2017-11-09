package com.riking.calendar.adapter;

import android.content.Context;
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
        h.industryName.setText(c.reportName + " +");
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String industryName = h.industryName.getText().toString();
                if (h.checkImage.getVisibility() == View.VISIBLE) {
                    h.checkImage.setVisibility(View.GONE);
                    h.industryName.setText(industryName + "+");
                    h.industryName.setTextColor(h.industryName.getResources().getColor(R.color.black_deep));
                    h.root.setBackground(h.root.getResources().getDrawable(R.drawable.not_selected__interesting_reports_background));
                } else {
                    h.checkImage.setVisibility(View.VISIBLE);
                    h.industryName.setText(industryName.subSequence(0, industryName.indexOf('+')));
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
