package com.riking.calendar.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.viewholder.base.ZViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw.zhang on 2017/12/15.
 */

public abstract class ZAdater<VH extends RecyclerView.ViewHolder, ItemBean> extends RecyclerView.Adapter<VH> {
    public List<ItemBean> mList;

    {
        mList = new ArrayList<>();
    }

    public ZAdater() {
    }

    public ZAdater(List<ItemBean> r) {
        mList = r;
    }

    public void setData(List<ItemBean> mList) {
        if (mList == null) return;
        MyLog.d("set data size: " + mList.size());
//        this.mList.clear();
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void addAllAtStart(List<ItemBean> list) {
        mList.addAll(0, list);
        notifyDataSetChanged();
//        notifyItemRangeInserted(0, list.size());
    }

    public void addAllAtEnd(List<ItemBean> list) {
        int size = mList.size();
        mList.addAll(size, list);
        notifyDataSetChanged();
//        notifyItemRangeInserted(size - 1, mList.size());
    }

    public void appendStart(ItemBean itemBean) {
        mList.add(0, itemBean);
        notifyDataSetChanged();
    }

    public void remmoveItem(ItemBean itemBean, int position) {
        mList.remove(itemBean);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateVH(parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (holder instanceof ZViewHolder) {
            ZViewHolder h = (ZViewHolder) holder;
            if (h.divider != null) {
                //hide the divider of the last item.
                if (position == mList.size() - 1) {
                    h.divider.setVisibility(View.GONE);
                } else {
                    h.divider.setVisibility(View.VISIBLE);
                }
            }
        }

        onBindVH(holder, position);
    }

    public abstract void onBindVH(VH holder, int position);

    public abstract VH onCreateVH(ViewGroup parent, int viewType);

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
