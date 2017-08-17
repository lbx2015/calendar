package com.riking.calendar.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.activity.RemindRepeatActivity;
import com.riking.calendar.activity.RemindWayActivity;
import com.riking.calendar.widget.dialog.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class CreateReminderFragment extends Fragment implements View.OnClickListener {

    public static int REPEAT_ITEMS = 1;
    public static int REMIND_WAY_ITEMS = 2;
    //1...7
    public String repeatWeek;
    public String title;
    public String remiderTime;
    //timeView
    public Calendar time;
    public EditText remindTitle;
    public byte repeatFlag;
    public byte isRemind = 1;
    public int aheadTime;
    public byte isAllDay;

    public Switch allDaySwitch;
    public TextView remindTimeTextView;
    SharedPreferences preference;
    //    private WheelPopWindow popWindow;
    private TimePickerDialog pickerDialog;
    private View selectRemindTime;
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
        remindTimeTextView = (TextView) v.findViewById(R.id.select_time);
        repeat = v.findViewById(R.id.repeat_item);
        way = v.findViewById(R.id.way_item);
        allDaySwitch = (Switch) v.findViewById(R.id.simpleSwitch);

        way.setOnClickListener(this);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        time = Calendar.getInstance();
        if (remiderTime == null) {
            time.setTime(new Date());
            //set the default timeView to be 2 hours later comparing current timeView.
            time.set(Calendar.HOUR, time.get(Calendar.HOUR) + 2);
            time.set(Calendar.MINUTE, 0);
            remindTimeTextView.setText(sdf.format(time.getTime()));
        } else {
            try {
                time.setTime(sdf.parse(remiderTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (isAllDay == 1) {
                remindTimeTextView.setText(remiderTime.substring(0, 10));
//                remindTime.setText(remiderTime.substring(0, 10));
            } else {
                remindTimeTextView.setText(remiderTime);
            }
        }


        pickerDialog = new TimePickerDialog(getContext());
        selectRemindTime.setOnClickListener(this);
        pickerDialog.btnSubmit.setOnClickListener(this);
        pickerDialog.btnCancel.setOnClickListener(this);
        repeat.setOnClickListener(this);
        preference = getActivity().getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);

        allDaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            boolean skipFirstTime;

            {
                //Fix the first timeView when call switch toggle method , the checked method is invoked.
                if (isAllDay == 1) {
                    skipFirstTime = true;
                }
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (skipFirstTime) {
                    skipFirstTime = false;
                    return;
                }
                if (isChecked) {
                    isAllDay = 1;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    time = Calendar.getInstance();
                    time.setTime(new Date());
                    //set the default timeView to be 2 hours later comparing current timeView.
                    time.set(Calendar.HOUR, 8);
                    remindTimeTextView.setText(sdf.format(time.getTime()));
                } else {
                    isAllDay = 0;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    time = Calendar.getInstance();
                    time.setTime(new Date());
                    //set the default timeView to be 2 hours later comparing current timeView.
                    time.set(Calendar.HOUR, time.get(Calendar.HOUR) + 2);
                    time.set(Calendar.MINUTE, 0);
                    remindTimeTextView.setText(sdf.format(time.getTime()));
                }
            }
        });

        //edit
        if (title != null) {
            remindTitle.setText(title);
//            remindTime.setText(remiderTime);
            if (isAllDay == 1) {
                allDaySwitch.toggle();
            }
        }
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
                time.set(Calendar.DATE, Integer.parseInt(pickerDialog.wheelDatePicker.day));
                if (isAllDay == 1) {
                    //24 hour format
                    time.set(Calendar.HOUR_OF_DAY, preference.getInt(Const.WHOLE_DAY_EVENT_HOUR, 8));
                    time.set(Calendar.MINUTE, preference.getInt(Const.WHOLE_DAY_EVENT_MINUTE, 0));
                } else {
                    time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(pickerDialog.wheelTimePicker.hour));
                    time.set(Calendar.MINUTE, Integer.parseInt(pickerDialog.wheelTimePicker.minute));
                }

                SimpleDateFormat sdf = isAllDay == 1 ? new SimpleDateFormat("yyyy-MM-dd") : new SimpleDateFormat("yyyy-MM-dd HH:mm");
                remindTimeTextView.setText(sdf.format(time.getTime()));
                remindTimeTextView.invalidate();
                pickerDialog.dismiss();
                break;
            }
            case R.id.btnCancel: {
                pickerDialog.dismiss();
                break;
            }
            case R.id.way_item: {
                Intent i = new Intent(getActivity(), RemindWayActivity.class);
                i.putExtra("is_remind", isRemind);
                i.putExtra("ahead_time", aheadTime);
                startActivityForResult(i, REMIND_WAY_ITEMS);
                break;
            }
            case R.id.repeat_item: {
                Intent i = new Intent(getActivity(), RemindRepeatActivity.class);
                i.putExtra("repeat_flag", repeatFlag);
                i.putExtra("repeat_week", repeatWeek);
                startActivityForResult(i, REPEAT_ITEMS);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Make sure the request was successful
        if (resultCode != RESULT_CANCELED) {
            Bundle b = data.getExtras();
            // Check which request we're responding to
            if (requestCode == REPEAT_ITEMS) {
                repeatFlag = b.getByte("repeatWay");
                repeatWeek = b.getString("repeatWeekDays");
            } else if (requestCode == REMIND_WAY_ITEMS) {
                isRemind = b.getByte("isRemind");
                aheadTime = b.getInt("aheadOfTime");
            }
        }
    }
}
