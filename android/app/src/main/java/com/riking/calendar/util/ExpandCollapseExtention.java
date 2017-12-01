package com.riking.calendar.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zw.zhang on 2017/12/1.
 */

public class ExpandCollapseExtention {

    public static void expand(View view) {
        view.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(view, 0, view.getMeasuredHeight());
        mAnimator.start();
    }


    public static void collapse(final View view) {
        int finalHeight = view.getHeight();

        ValueAnimator mAnimator = slideAnimator(view, finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
            }


            @Override
            public void onAnimationStart(Animator animation) {

            }


            @Override
            public void onAnimationCancel(Animator animation) {

            }


            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();
    }


    private static ValueAnimator slideAnimator(final View v, int start, int end) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
