package com.riking.calendar.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.widget.dialog.TimeClockPickerDialog;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    //timeView
    String hour, minute;

    TextView wholeDayEventTime;
    SharedPreferences preferences;
    private TimeClockPickerDialog pickerDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_setting);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.login_out_button).setOnClickListener(this);
        findViewById(R.id.whole_day_event_time_relative_layout).setOnClickListener(this);
        wholeDayEventTime = (TextView) findViewById(R.id.event_time);

        pickerDialog = new TimeClockPickerDialog(this);
        pickerDialog.btnSubmit.setOnClickListener(this);
        pickerDialog.btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
            case R.id.login_out_button: {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();
            }
            case R.id.whole_day_event_time_relative_layout: {
                pickerDialog.show();
                break;
            }

            case R.id.btnSubmit: {
                hour = pickerDialog.wheelTimePicker.hour;
                minute = pickerDialog.wheelTimePicker.minute;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(Const.WHOLE_DAY_EVENT_HOUR, Integer.parseInt(hour));
                editor.putInt(Const.WHOLE_DAY_EVENT_MINUTE, Integer.parseInt(minute));
                wholeDayEventTime.setText(hour + ":" + minute);
                editor.commit();
                pickerDialog.dismiss();
                break;
            }
            case R.id.btnCancel: {
                pickerDialog.dismiss();
                break;
            }
        }
    }
}
