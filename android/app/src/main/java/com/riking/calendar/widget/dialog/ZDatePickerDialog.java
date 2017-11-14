package com.riking.calendar.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ImageView;

import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.util.ZR;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelPicker;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelYearMonthPicker;

/**
 * Created by zw.zhang on 2017/7/26.
 */

public class ZDatePickerDialog extends BottomSheetDialog implements AbstractWheelPicker.OnWheelChangeListener {
    public WheelYearMonthPicker wheelDatePicker;
    public View btnSubmit, btnCancel;
    public View wholeMonth;
    public boolean isWholeMonth;
    public ImageView wholeMonthImage;
    private String currentData;

    public ZDatePickerDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.pick_date_wheel);

        wheelDatePicker = (WheelYearMonthPicker) findViewById(R.id.date_picker);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        wholeMonth = findViewById(R.id.whole_month);
        wholeMonthImage = (ImageView) findViewById(R.id.whole_month_image);

        wheelDatePicker.pickerMonth.setVisibility(View.GONE);
        wheelDatePicker.setItemSpace((int) ZR.convertDpToPx(getContext(), 20f));
        wholeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWholeMonth) {
                    wheelDatePicker.pickerMonth.setVisibility(View.GONE);
                    isWholeMonth = false;
                    wholeMonthImage.setImageDrawable(getContext().getDrawable(R.drawable.not_checked_whole_month));
                    Logger.d("zzw", "whole month not checked");

                } else {
                    Logger.d("zzw", "whole month checked");
                    isWholeMonth = true;
                    wholeMonthImage.setImageDrawable(getContext().getDrawable(R.drawable.checked_whole_month));
                    //show month
                    wheelDatePicker.pickerMonth.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public ZDatePickerDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected ZDatePickerDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
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
