package com.necer.ncalendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/11/23.
 */

public class SimpleDividerDecorationWithoutLast extends RecyclerView.ItemDecoration {
    private final Rect bounds = new Rect();
    private Drawable divider;

    public SimpleDividerDecorationWithoutLast(Context context) {
        divider = context.getResources().getDrawable(R.drawable.recycler_view_divider);
    }

    public SimpleDividerDecorationWithoutLast(Drawable divider) {
        this.divider = divider;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        final int left = 0;
        final int right = parent.getWidth();
        final int childCount = parent.getChildCount() - 1;
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, bounds);
            final int bottom = bounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - divider.getIntrinsicHeight();
            divider.setBounds(left, top, right, bottom);
            divider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        final int lastPosition = parent.getAdapter().getItemCount() - 1;
        if (position < lastPosition) {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        }
    }
}