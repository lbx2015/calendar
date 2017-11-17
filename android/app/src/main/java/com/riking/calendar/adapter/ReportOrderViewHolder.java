package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xiangzhihong on 2016/3/2 on 15:49.
 */
public class ReportOrderViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.report_title)
    public TextView reportTitle;

    @BindView(R.id.report_name)
    public TextView reportName;

    @BindView(R.id.order)
    public TextView orderButton;

    public boolean checked;

    public ReportOrderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }
}
