package com.riking.calendar.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelPicker;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelMinutePicker;

/**
 * Created by zw.zhang on 2017/7/26.
 */

public class MinutePickerDialog extends BottomSheetDialog implements AbstractWheelPicker.OnWheelChangeListener {
    public WheelMinutePicker wheelTimePicker;
    public View btnSubmit, btnCancel;
    public String currentData;

    public MinutePickerDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_minute_pw);

        wheelTimePicker = (WheelMinutePicker) findViewById(R.id.time_picker);
        wheelTimePicker.setDigitType(2);
        wheelTimePicker.setOnWheelChangeListener(this);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public MinutePickerDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected MinutePickerDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
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
