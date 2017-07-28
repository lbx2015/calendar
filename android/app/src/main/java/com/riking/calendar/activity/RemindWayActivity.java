package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.widget.wheelpicker.widget.curved.WheelMinutePicker;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class RemindWayActivity extends AppCompatActivity implements View.OnClickListener {
    WheelMinutePicker wmp;
    View minutePickerItem;
    private View backButton;
    private View notRemind;
    private View remindAccurately;
    private View customRemind;
    private View notRemindImage;
    private View accurateRemindImage;
    private View customRemindImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_kind);
        wmp = (WheelMinutePicker) findViewById(R.id.minute_picker);
        backButton = findViewById(R.id.back);
        notRemind = findViewById(R.id.not_remind_item);
        remindAccurately = findViewById(R.id.remind_accurate_item);
        customRemind = findViewById(R.id.custom_remind_item);
        notRemindImage = findViewById(R.id.no_remind_confirm);
        accurateRemindImage = findViewById(R.id.remind_on_intergral_clock_confirm);
        customRemindImage = findViewById(R.id.remind_accurate_confirm);
        minutePickerItem = findViewById(R.id.minute_picker_item);

        notRemind.setOnClickListener(this);
        remindAccurately.setOnClickListener(this);
        customRemind.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
            case R.id.not_remind_item: {
                if (notRemindImage.getVisibility() == View.VISIBLE) break;
                notRemindImage.setVisibility(View.VISIBLE);
                accurateRemindImage.setVisibility(View.GONE);
                customRemindImage.setVisibility(View.GONE);
                minutePickerItem.setVisibility(View.GONE);
                break;
            }
            case R.id.remind_accurate_item: {
                if (accurateRemindImage.getVisibility() == View.VISIBLE) break;
                notRemindImage.setVisibility(View.GONE);
                accurateRemindImage.setVisibility(View.VISIBLE);
                customRemindImage.setVisibility(View.GONE);
                minutePickerItem.setVisibility(View.GONE);
                break;
            }
            case R.id.custom_remind_item: {
                if (customRemindImage.getVisibility() == View.VISIBLE) break;
                notRemindImage.setVisibility(View.GONE);
                accurateRemindImage.setVisibility(View.GONE);
                customRemindImage.setVisibility(View.VISIBLE);
                minutePickerItem.setVisibility(View.VISIBLE);
                break;
            }
        }

    }
}
