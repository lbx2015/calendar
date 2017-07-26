package com.riking.calendar.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelPicker;
import com.riking.calendar.widget.wheelpicker.view.WheelCurvedPicker;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelDatePicker;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelTimePicker;

import java.util.List;

/**
 * 在此写用途
 *
 * @FileName: me.khrystal.widget.WheelPopWindow.java
 * @author: kHRYSTAL
 * @email: 723526676@qq.com
 * @date: 2016-01-13 12:33
 */
public class WheelPopWindow extends PopupWindow implements AbstractWheelPicker.OnWheelChangeListener {
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    public WheelDatePicker wheelDatePicker;
    public WheelTimePicker wheelTimePicker;
    private View rootView;
    public View btnSubmit, btnCancel;
    private List<String> mData;
    private WheelCurvedPicker mPicker;
    private WheelCurvedPicker mPicker2;
    private String currentData;

    public WheelPopWindow(Context context, List<String> data) {
        super(context);
        mData = data;
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // Closes the popup window when touch outside.
        setOutsideTouchable(true);
        setFocusable(true);
        // Removes default background. Fix the issue when clicking the anchor the pop up view hide and show again.
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setAnimationStyle(R.style.timepopwindow_anim_style);
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(R.layout.pw_wheel, null);
        wheelDatePicker = (WheelDatePicker) rootView.findViewById(R.id.date_picker);
        wheelTimePicker = (WheelTimePicker) rootView.findViewById(R.id.time_picker);
        btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnCancel = rootView.findViewById(R.id.btnCancel);
        setContentView(rootView);

    }

    public WheelPopWindow(Context context) {
        this(context, null);
    }

    @Override
    public void onWheelScrolling(float deltaX, float deltaY) {

    }

    @Override
    public void onWheelSelected(int index, String data) {
        currentData = data;
    }

    @Override
    public void onWheelScrollStateChanged(int state) {

    }
}
