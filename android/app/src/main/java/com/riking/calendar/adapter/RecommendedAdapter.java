package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.fragment.PlazaFragment;

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

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {

        public RecommendedViewHolder(View itemView) {
            super(itemView);
        }
    }
}
