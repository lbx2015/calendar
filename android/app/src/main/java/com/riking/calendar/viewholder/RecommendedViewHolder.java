package com.riking.calendar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/10/11.
 */

public class RecommendedViewHolder extends RecyclerView.ViewHolder {
    public RecyclerView recyclerView;

    public RecommendedViewHolder(View itemView) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.recycler_view);
    }
}
