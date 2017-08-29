package com.riking.calendar.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by zw.zhang on 2017/8/24.
 * This custom image view not scale.
 * using ScaleType.Center by default.
 */

public class ZCenterImageView extends android.support.v7.widget.AppCompatImageView {

    public ZCenterImageView(Context context) {
        super(context);
        init();
    }

    public ZCenterImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZCenterImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER);
    }
}
