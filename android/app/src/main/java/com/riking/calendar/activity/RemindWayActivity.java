package com.riking.calendar.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.riking.calendar.R;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelDecor;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelMinutePicker;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class RemindWayActivity extends AppCompatActivity {
    WheelMinutePicker wmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_kind);
        wmp = (WheelMinutePicker) findViewById(R.id.minute_picker);
        /*wmp.setWheelDecor(true, new AbstractWheelDecor() {
            @Override
            public void drawDecor(Canvas canvas, Rect rectLast, Rect rectNext, Paint paint) {
                paint.setColor(Color.BLACK);
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.normal_font_size_18));
                canvas.drawText("分", rectNext.centerX(),
                        rectNext.centerY() - (paint.ascent() + paint.descent()) / 2.0F, paint);
                canvas.drawText("提前", rectLast.centerX(), rectLast.centerY() - (paint.ascent() + paint.descent()) / 2.0F, paint);
            }
        });*/
    }
}
