package com.riking.calendar.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelPicker;
import com.riking.calendar.widget.wheelpicker.view.WheelCurvedPicker;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelDatePicker;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelTimePicker;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/26.
 */

public class TimePickerDialog extends BottomSheetDialog implements AbstractWheelPicker.OnWheelChangeListener {
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    public WheelDatePicker wheelDatePicker;
    public WheelTimePicker wheelTimePicker;
    public View btnSubmit, btnCancel;
    private View rootView;
    private List<String> mData;
    private WheelCurvedPicker mPicker;
    private WheelCurvedPicker mPicker2;
    private String currentData;

    public TimePickerDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.pw_wheel);

        wheelDatePicker = (WheelDatePicker) findViewById(R.id.date_picker);
        wheelTimePicker = (WheelTimePicker) findViewById(R.id.time_picker);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

    }

    public TimePickerDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected TimePickerDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
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
