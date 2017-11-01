package com.riking.calendar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by zw.zhang on 2017/9/8.
 */

public class ZRecyclerView extends RecyclerView {
    public EmptyViewCallBack emptyViewCallBack;

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public ZRecyclerView(Context context) {
        super(context);
    }

    public ZRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ZRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    void checkIfEmpty() {
        if (emptyViewCallBack != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            if (emptyViewVisible) {
                emptyViewCallBack.onEmpty();
            } else {
                emptyViewCallBack.onNotEmpty();
            }
        }
    }

    public interface EmptyViewCallBack {
        void onEmpty();

        void onNotEmpty();
    }
}
