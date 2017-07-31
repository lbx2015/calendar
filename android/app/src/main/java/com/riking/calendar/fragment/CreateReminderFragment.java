package com.riking.calendar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.RemindRepeatActivity;
import com.riking.calendar.activity.RemindWayActivity;
import com.riking.calendar.widget.dialog.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class CreateReminderFragment extends Fragment implements View.OnClickListener {
    //time
    public Calendar time;
    public EditText remindTitle;
    //    private WheelPopWindow popWindow;
    private TimePickerDialog pickerDialog;
    private View selectRemindTime;
    private TextView remindTime;
    //reminder repeat item
    private View repeat;
    //reminder way item
    private View way;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_reminder_fragment, container, false);
        remindTitle = (EditText) v.findViewById(R.id.remind_title);
//        popWindow = new WheelPopWindow(getContext());
        selectRemindTime = v.findViewById(R.id.select_remind_time);
//        popWindow.btnSubmit.setOnsetOnClickListenerClickListener(this);
//        popWindow.btnCancel.(this);
        remindTime = (TextView) v.findViewById(R.id.select_time);
        repeat = v.findViewById(R.id.repeat_item);
        way = v.findViewById(R.id.way_item);
        way.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        time = Calendar.getInstance();
        time.setTime(new Date());
        //set the default time to be 2 hours later comparing current time.
        time.set(Calendar.HOUR, time.get(Calendar.HOUR) + 2);
        time.set(Calendar.MINUTE, 0);
        remindTime.setText(sdf.format(time.getTime()));

        pickerDialog = new TimePickerDialog(getContext());
        selectRemindTime.setOnClickListener(this);
        pickerDialog.btnSubmit.setOnClickListener(this);
        pickerDialog.btnCancel.setOnClickListener(this);
        repeat.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_remind_time: {
                Log.d("zzw", "click the remind.");
//                popWindow.showAtLocation(selectRemindTime, Gravity.BOTTOM, 0, 0);
                pickerDialog.show();
                break;
            }
            case R.id.btnSubmit: {
                time.set(Calendar.YEAR, Integer.parseInt(pickerDialog.wheelDatePicker.year));
                time.set(Calendar.MONTH, Integer.parseInt(pickerDialog.wheelDatePicker.month) - 1);
                time.set(Calendar.DATE, Integer.parseInt(pickerDialog.wheelDatePicker.day) - 1);
                time.set(Calendar.HOUR, Integer.parseInt(pickerDialog.wheelTimePicker.hour));
                time.set(Calendar.MINUTE, Integer.parseInt(pickerDialog.wheelTimePicker.minute));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                remindTime.setText(sdf.format(time.getTime()));
                pickerDialog.dismiss();
                break;
            }
            case R.id.btnCancel: {
                pickerDialog.dismiss();
                break;
            }
            case R.id.way_item: {
                startActivity(new Intent(getActivity(), RemindWayActivity.class));
                break;
            }
            case R.id.repeat_item: {
                startActivity(new Intent(getActivity(), RemindRepeatActivity.class));
            }
        }
    }
}
