package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.OrderReportActivity;
import com.riking.calendar.pojo.server.BaseModelPropdict;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.OneTextViewHolder;

import java.util.ArrayList;
import java.util.List;


public class ReportFrequencyAdapter extends RecyclerView.Adapter<OneTextViewHolder> {

    public List<BaseModelPropdict> mList;
    private OrderReportActivity activity;
    //the default checked position is 0;
    private int checkedPosition = 0;

    public ReportFrequencyAdapter(OrderReportActivity activity) {
        this.activity = activity;
        mList = new ArrayList<>();
    }

    @Override
    public OneTextViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.report_frequency_item, viewGroup, false);
        return new OneTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OneTextViewHolder h, final int i) {
        final BaseModelPropdict m = mList.get(i);
        h.textView.setText(m.valueName);
        setColors(h, i);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final int temp = checkedPosition;
                checkedPosition = i;
                //only update the the two columns
                notifyItemChanged(checkedPosition);
                notifyItemChanged(temp);
                //update the report list
                activity.reportsOrderAdapter.setData(activity.reportAgences.get(activity.orgonizeType).list.get(i).list);
            }
        });
    }

    private void setColors(OneTextViewHolder h, int position) {
        if (checkedPosition == position) {
            //set the background color
            h.itemView.setBackgroundColor(ZR.getColor(R.color.white));
            //set the text color
            h.textView.setTextColor(ZR.getColor(R.color.color_222222));
        } else {
            //set the background color
            h.itemView.setBackgroundColor(ZR.getColor(R.color.color_f4f4f4));
            //set the text color
            h.textView.setTextColor(ZR.getColor(R.color.color_666666));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<BaseModelPropdict> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
