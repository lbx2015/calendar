package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;

import java.util.ArrayList;
import java.util.List;


public class InterestingReportAdapter extends RecyclerView.Adapter<InterestingReportHolder> {

    public List<String> mList;
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
        if (i == 0) {
            h.industryName.setText("存款准备金+");
        } else if (i == 2) {
            h.industryName.setText("1104季1+");
        } else if (i == 3) {
            h.industryName.setText("打集中 月1 +");
        } else {
            h.industryName.setText("利率保备+");
        }
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.checkImage.getVisibility() == View.VISIBLE) {
                    h.checkImage.setVisibility(View.GONE);
                    h.industryName.setTextColor(h.industryName.getResources().getColor(R.color.black_deep));
                } else {
                    h.checkImage.setVisibility(View.VISIBLE);
                    h.root.setBackgroundColor(h.root.getResources().getColor(R.color.white));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
