package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.widget.WheelPopWindow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class CreateReminderFragment extends Fragment implements View.OnClickListener {

    private WheelPopWindow popWindow;
    private View selectRemindTime;
    private TextView remindTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_reminder_fragment, container, false);
        popWindow = new WheelPopWindow(getContext());
        selectRemindTime = v.findViewById(R.id.select_remind_time);
        selectRemindTime.setOnClickListener(this);
        popWindow.btnSubmit.setOnClickListener(this);
        popWindow.btnCancel.setOnClickListener(this);
        remindTime = (TextView) v.findViewById(R.id.select_time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        //set the default time to be 2 hours later comparing current time.
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) + 2);
        c.set(Calendar.MINUTE, 0);
        remindTime.setText(sdf.format(c.getTime()));
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_remind_time: {
                Log.d("zzw", "click the remind.");
                if (popWindow.isShowing()) {
                    Log.d("zzw", "pop window is showing.");
                    popWindow.dismiss();
                    return;
                }
                popWindow.showAtLocation(selectRemindTime, Gravity.BOTTOM, 0, 0);
                break;
            }
            case R.id.btnSubmit: {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, Integer.parseInt(popWindow.wheelDatePicker.year));
                c.set(Calendar.MONTH, Integer.parseInt(popWindow.wheelDatePicker.month) - 1);
                c.set(Calendar.DATE, Integer.parseInt(popWindow.wheelDatePicker.day) - 1);
                c.set(Calendar.HOUR, Integer.parseInt(popWindow.wheelTimePicker.hour));
                c.set(Calendar.MINUTE, Integer.parseInt(popWindow.wheelTimePicker.minute));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                remindTime.setText(sdf.format(c.getTime()));
                popWindow.dismiss();
                break;
            }
            case R.id.btnCancel: {
                popWindow.dismiss();
                break;
            }
        }
    }
}
