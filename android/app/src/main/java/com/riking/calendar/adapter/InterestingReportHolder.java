package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xiangzhihong on 2016/3/2 on 15:49.
 */
public class InterestingReportHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.interesting_report)
    public TextView reportName;
    @BindView(R.id.check_image)
    public ImageView checkImage;
    @BindView(R.id.item_view)
    View root;
    boolean checked = false;

    public InterestingReportHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }
}