package com.riking.calendar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/11/10.
 */

public class OrderReportFrameLayout extends FrameLayout {
    //Whether the report is checked
    public boolean checked;
    public TextView reportNameTV;
    public ImageView checkImage;
    public OrderReportFrameLayout(Context context) {
        super(context);
    }

    public OrderReportFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OrderReportFrameLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OrderReportFrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init() {

        //get view
        reportNameTV = findViewById(R.id.interesting_report);
        checkImage = findViewById(R.id.check_image);
    }
}
