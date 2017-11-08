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
public class PositionViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.industryName)
    public TextView industryName;
    @BindView(R.id.check_image)
    public ImageView checkImage;

    public PositionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }
}
