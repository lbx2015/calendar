package com.riking.calendar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.viewholder.base.ZViewHolder;

/**
 * Created by zw.zhang on 2017/10/11.
 */

public class RecommendedViewHolder extends ZViewHolder {
    public RecyclerView recyclerView;
    public TextView titleTV;

    public RecommendedViewHolder(View itemView) {
        super(itemView);
        titleTV = itemView.findViewById(R.id.title);
        recyclerView = itemView.findViewById(R.id.recycler_view);
    }
}
