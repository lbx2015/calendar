package com.riking.calendar.widget.wheelpicker.widget.curved;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.riking.calendar.R;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelDecor;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelPicker;
import com.riking.calendar.widget.wheelpicker.core.IWheelPicker;
import com.riking.calendar.widget.wheelpicker.view.WheelCrossPicker;

import java.util.List;

/**
 * 基于WheelPicker的日期选择控件
 * DatePicker base on WheelPicker
 *
 * @author AigeStudio 2015-12-03
 * @author AigeStudio 2015-12-08
 *         Init初始化
 * @author AigeStudio 2015-12-12
 *         实现变更
 * @version 1.0.0 beta
 */
public class WheelYearMonthPicker extends LinearLayout implements IWheelPicker {
    public WheelYearPicker pickerYear;
    public WheelMonthPicker pickerMonth;
    public String year, month;
    protected AbstractWheelPicker.OnWheelChangeListener listener;
    protected int labelColor = 0xFF000000;
    protected int stateYear, stateMonth, stateDay;

    protected float labelTextSize;

    public WheelYearMonthPicker(Context context) {
        super(context);
        init();
    }

    public WheelYearMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);

        int padding = getResources().getDimensionPixelSize(R.dimen.WheelPadding);
        int padding2x = padding * 2;

        labelTextSize = padding;

        LayoutParams llParams = new LayoutParams(-2, -2);
        llParams.gravity = Gravity.CENTER;

        pickerYear = new WheelYearPicker(getContext());
        pickerMonth = new WheelMonthPicker(getContext());
        pickerYear.setPadding(0, padding, padding2x, padding);
        pickerMonth.setPadding(0, padding, padding2x, padding);

        addView(pickerYear, llParams);
        addView(pickerMonth, llParams);

        initListener(pickerYear, 0);
        initListener(pickerMonth, 1);
    }

    private void addLabel(WheelCrossPicker picker, final String label) {
        picker.setWheelDecor(true, new AbstractWheelDecor() {
            @Override
            public void drawDecor(Canvas canvas, Rect rectLast, Rect rectNext, Paint paint) {
                paint.setColor(labelColor);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(labelTextSize * 1.5F);
                canvas.drawText(label, rectNext.centerX(),
                        rectNext.centerY() - (paint.ascent() + paint.descent()) / 2.0F, paint);
            }
        });
    }

    private void initListener(final WheelCrossPicker picker, final int type) {
        picker.setOnWheelChangeListener(new AbstractWheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolling(float deltaX, float deltaY) {
                if (null != listener) listener.onWheelScrolling(deltaX, deltaY);
            }

            @Override
            public void onWheelSelected(int index, String data) {
                if (type == 0) year = data;
                if (type == 1) month = data;
            }

            @Override
            public void onWheelScrollStateChanged(int state) {
                if (type == 0) stateYear = state;
                if (type == 1) stateMonth = state;
            }
        });
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
        invalidate();
    }

    public void setLabelTextSize(float labelTextSize) {
        this.labelTextSize = labelTextSize;
        invalidate();
    }

    @Override
    public void setData(List<String> data) {
        throw new RuntimeException("Set data will not allow here!");
    }

    public void setCurrentDate(int year, int month, int day) {
        pickerYear.setCurrentYear(year);
        pickerMonth.setCurrentMonth(month);
    }

    @Override
    public void setOnWheelChangeListener(AbstractWheelPicker.OnWheelChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void setItemIndex(int index) {
        pickerYear.setItemIndex(index);
        pickerMonth.setItemIndex(index);
    }

    @Override
    public void setItemSpace(int space) {
        pickerYear.setItemSpace(space);
        pickerMonth.setItemSpace(space);
    }

    @Override
    public void setItemCount(int count) {
        pickerYear.setItemCount(count);
        pickerMonth.setItemCount(count);
    }

    @Override
    public void setTextColor(int color) {
        pickerYear.setTextColor(color);
        pickerMonth.setTextColor(color);
    }

    @Override
    public void setTextSize(int size) {
        pickerYear.setTextSize(size);
        pickerMonth.setTextSize(size);
    }

    @Override
    public void clearCache() {
        pickerYear.clearCache();
        pickerMonth.clearCache();
    }

    @Override
    public void setCurrentTextColor(int color) {
        pickerYear.setCurrentTextColor(color);
        pickerMonth.setCurrentTextColor(color);
    }

    @Override
    public void setWheelDecor(boolean ignorePadding, AbstractWheelDecor decor) {
        pickerYear.setWheelDecor(ignorePadding, decor);
        pickerMonth.setWheelDecor(ignorePadding, decor);
    }
}