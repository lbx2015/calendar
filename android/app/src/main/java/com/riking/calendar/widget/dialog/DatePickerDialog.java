package com.riking.calendar.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Switch;

import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelPicker;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelDatePicker;

/**
 * Created by zw.zhang on 2017/7/26.
 */

public class DatePickerDialog extends BottomSheetDialog implements AbstractWheelPicker.OnWheelChangeListener {
    public WheelDatePicker wheelDatePicker;
    public View btnSubmit, btnCancel;
    public Switch wholeMonth;
    public Switch wholeYear;
    public Switch clearDateFilter;
    public boolean isWholeMonth;
    public boolean isWholeYear;
    public boolean isDateFilterCleared;
    private String currentData;

    public DatePickerDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.pick_date_wheel);

        wheelDatePicker = (WheelDatePicker) findViewById(R.id.date_picker);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        wholeMonth = (Switch) findViewById(R.id.whole_month);
        wholeYear = (Switch) findViewById(R.id.whole_year);
        clearDateFilter = (Switch) findViewById(R.id.clear_date_filter);

        wholeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wholeMonth.isChecked()) {
                    Logger.d("zzw", "whole month checked");
                    isWholeMonth = true;
                    clearDateFilter.setChecked(false);
                    isDateFilterCleared = false;
                    wholeYear.setChecked(false);
                    isWholeYear = false;
                    //show month
                    wheelDatePicker.pickerMonth.setVisibility(View.VISIBLE);
                    //hide day
                    wheelDatePicker.pickerDay.setVisibility(View.GONE);
                } else {
                    wheelDatePicker.pickerDay.setVisibility(View.VISIBLE);
                    wheelDatePicker.pickerMonth.setVisibility(View.VISIBLE);
                    isWholeMonth = false;
                    Logger.d("zzw", "whole month not checked");
                }
            }
        });

        wholeYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wholeYear.isChecked()) {
                    Logger.d("zzw", "whole month checked");
                    isWholeYear = true;
                    clearDateFilter.setChecked(false);
                    isDateFilterCleared = false;
                    wholeMonth.setChecked(false);
                    isWholeMonth = false;
                    //hide week
                    wheelDatePicker.pickerDay.setVisibility(View.GONE);
                    wheelDatePicker.pickerMonth.setVisibility(View.GONE);
                } else {
                    isWholeYear = false;
                    wheelDatePicker.pickerDay.setVisibility(View.VISIBLE);
                    wheelDatePicker.pickerMonth.setVisibility(View.VISIBLE);
                    Logger.d("zzw", "whole month not checked");
                }
            }
        });

        clearDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clearDateFilter.isChecked()) {
                    Logger.d("zzw", "clear date filter checked");
                    isDateFilterCleared = true;
                    wholeYear.setChecked(false);
                    isWholeYear = false;
                    wholeMonth.setChecked(false);
                    isWholeMonth = false;
                    wheelDatePicker.pickerDay.setVisibility(View.VISIBLE);
                    wheelDatePicker.pickerMonth.setVisibility(View.VISIBLE);
                } else {
                    Logger.d("zzw", "clear date filter is not checked.");
                    isDateFilterCleared = false;
                }
            }
        });
    }

    public DatePickerDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected DatePickerDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
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
