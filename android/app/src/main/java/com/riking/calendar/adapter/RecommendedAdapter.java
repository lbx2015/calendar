package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.riking.calendar.R;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/10/11.
 */

public class RecommendedAdapter extends RecyclerView.Adapter {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_row, parent, false);
        return new RecommendedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            //adding protection
            if (holder.itemView instanceof RelativeLayout) {
                RelativeLayout root = (RelativeLayout) holder.itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) root.getLayoutParams();
                layoutParams.leftMargin = (int) ZR.convertDpToPx(15);
            }
        } else if (position == (getItemCount() - 1)) {
            RelativeLayout root = (RelativeLayout) holder.itemView;
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) root.getLayoutParams();
            layoutParams.rightMargin = (int) ZR.convertDpToPx(15);
        } else {
            if (holder.itemView instanceof RelativeLayout) {
                RelativeLayout root = (RelativeLayout) holder.itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) root.getLayoutParams();
                layoutParams.leftMargin = (int) ZR.convertDpToPx(6);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout root;

        public RecommendedViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
        }
    }
}
