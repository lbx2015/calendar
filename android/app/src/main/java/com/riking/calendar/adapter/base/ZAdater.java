package com.riking.calendar.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.viewholder.base.ZViewHolder;

/**
 * Created by zw.zhang on 2017/12/15.
 */

public abstract class ZAdater<VH extends ZViewHolder> extends RecyclerView.Adapter<VH> {
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateVH(parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        //hide the divider of the last item.
        if (position == getCount() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }

        onBindVH(holder, position);
    }

    public abstract void onBindVH(VH holder, int position);

    public abstract VH onCreateVH(ViewGroup parent, int viewType);

    public abstract int getCount();

    @Override
    public int getItemCount() {
        return getCount();
    }
}
