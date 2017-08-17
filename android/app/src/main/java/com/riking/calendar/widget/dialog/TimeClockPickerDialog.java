package com.riking.calendar.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelPicker;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelTimePicker;

/**
 * Created by zw.zhang on 2017/7/26.
 */

public class TimeClockPickerDialog extends BottomSheetDialog implements AbstractWheelPicker.OnWheelChangeListener {
    public WheelTimePicker wheelTimePicker;
    public View btnSubmit, btnCancel;
    public String currentData;

    public TimeClockPickerDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.time_clock_pw_wheel);

        wheelTimePicker = (WheelTimePicker) findViewById(R.id.time_picker);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

    }

    public TimeClockPickerDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected TimeClockPickerDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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
