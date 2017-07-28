package com.riking.calendar.activity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.widget.wheelpicker.core.AbstractWheelDecor;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelMinutePicker;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class RemindWayActivity extends AppCompatActivity implements View.OnClickListener{
    WheelMinutePicker wmp;
    private View backButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_kind);
        wmp = (WheelMinutePicker) findViewById(R.id.minute_picker);
        backButton = findViewById(R.id.back);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:{
                onBackPressed();
                break;
            }
        }

    }
}
