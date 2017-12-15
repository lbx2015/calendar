package com.riking.calendar.viewholder.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/12/15.
 */

public class ZViewHolder extends RecyclerView.ViewHolder {
    public View divider;
    public ZViewHolder(View itemView) {
        super(itemView);
        divider = itemView.findViewById(R.id.divider);
    }
}
