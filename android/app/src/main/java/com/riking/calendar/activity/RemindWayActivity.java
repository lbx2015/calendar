package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.fragment.CreateReminderFragment;
import com.riking.calendar.widget.dialog.MinutePickerDialog;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class RemindWayActivity extends AppCompatActivity implements View.OnClickListener {
    MinutePickerDialog dialog;
    private View notRemindImage;
    private View accurateRemindImage;
    private View customRemindImage;
    private byte isRemind;
    private int aheadOfTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remind_kind);
        View backButton = findViewById(R.id.back);
        View notRemind = findViewById(R.id.not_remind_item);
        View remindAccurately = findViewById(R.id.remind_accurate_item);
        View customRemind = findViewById(R.id.custom_remind_item);
        notRemindImage = findViewById(R.id.no_remind_confirm);
        accurateRemindImage = findViewById(R.id.remind_on_intergral_clock_confirm);
        customRemindImage = findViewById(R.id.remind_accurate_confirm);

        //set on click listeners
        backButton.setOnClickListener(this);
        notRemind.setOnClickListener(this);
        remindAccurately.setOnClickListener(this);
        customRemind.setOnClickListener(this);
        findViewById(R.id.done).setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        isRemind = bundle.getByte("is_remind");
        aheadOfTime = bundle.getInt("ahead_time");
        if (isRemind == 1) {
            accurateRemindImage.setVisibility(View.VISIBLE);
            notRemindImage.setVisibility(View.GONE);
            customRemindImage.setVisibility(View.GONE);
        } else if (aheadOfTime > 0) {
            notRemindImage.setVisibility(View.GONE);
            accurateRemindImage.setVisibility(View.GONE);
            customRemindImage.setVisibility(View.VISIBLE);
        } else {
            notRemindImage.setVisibility(View.VISIBLE);
            accurateRemindImage.setVisibility(View.GONE);
            customRemindImage.setVisibility(View.GONE);
        }
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
                isRemind = 0;
                aheadOfTime = 0;
                notRemindImage.setVisibility(View.VISIBLE);
                accurateRemindImage.setVisibility(View.GONE);
                customRemindImage.setVisibility(View.GONE);
                break;
            }
            case R.id.remind_accurate_item: {
                if (accurateRemindImage.getVisibility() == View.VISIBLE) break;
                isRemind = 1;
                aheadOfTime = 0;
                notRemindImage.setVisibility(View.GONE);
                accurateRemindImage.setVisibility(View.VISIBLE);
                customRemindImage.setVisibility(View.GONE);
                break;
            }
            case R.id.custom_remind_item: {
                isRemind = 1;
                aheadOfTime = aheadOfTime;
                notRemindImage.setVisibility(View.GONE);
                accurateRemindImage.setVisibility(View.GONE);
                customRemindImage.setVisibility(View.VISIBLE);
                //show picker
                if (dialog == null) {
                    dialog = new MinutePickerDialog(this);
                }
                dialog.wheelTimePicker.setCurrentMinute(aheadOfTime);
                dialog.show();
                break;
            }
            case R.id.done: {
                Intent i = new Intent();
                i.putExtra("isRemind", isRemind);//0,1
                i.putExtra("aheadOfTime", dialog == null ? aheadOfTime : Integer.valueOf(dialog.currentData));
                setResult(CreateReminderFragment.REMIND_WAY_ITEMS, i);
                finish();
            }
        }
    }
}
