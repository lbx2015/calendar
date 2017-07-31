package com.riking.calendar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by zw.zhang on 2017/7/31.
 */

public class CustomLinearLayout extends LinearLayout {
    public OnDraggingListener onDraggingListener;
    private float mLastX;
    private float mLastY;
    private float mStartY;
    private boolean mIsBeingDragged;
    private float mTouchSlop = 20;

    public CustomLinearLayout(Context context) {
        super(context);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.d("zzw", "onInterceptTouchEvent: " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mStartY = mLastY;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                float xDelta = Math.abs(x - mLastX);
                float yDelta = Math.abs(y - mLastY);

                float yDeltaTotal = y - mStartY;
//                if (yDelta > xDelta && Math.abs(yDeltaTotal) > mTouchSlop) {
//                    mIsBeingDragged = true;
//                    mStartY = y;
//                }
                Log.d("zzw", "scrolled");
                if (yDelta > xDelta && Math.abs(yDeltaTotal) > mTouchSlop && onDraggingListener != null) {
                    if (yDeltaTotal < 0) {
                        Log.d("zzw", "scroll up");
                        onDraggingListener.scrollUp();
                    } else {
                        onDraggingListener.scrollDown();
                    }
                }

                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("zzw", "onTouchEvent: " + event.getAction());

        return super.onTouchEvent(event);
    }

    public interface OnDraggingListener {
        void scrollUp();

        void scrollDown();
    }
}
