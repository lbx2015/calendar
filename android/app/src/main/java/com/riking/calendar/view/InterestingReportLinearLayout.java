package com.riking.calendar.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by zw.zhang on 2017/11/10.
 */

public class InterestingReportLinearLayout extends LinearLayout {
    //Whether the report is checked
    public boolean checked;
    public InterestingReportLinearLayout(Context context) {
        super(context);
    }

    public InterestingReportLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterestingReportLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InterestingReportLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
