package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.widget.dialog.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class CreateToDoFragment extends Fragment implements View.OnClickListener {
    private TimePickerDialog pickerDialog;
    private TextView remindTime;
    private Switch aSwitch;
    private ImageView notImportant;
    private ImageView important;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_to_do_fragment, container, false);
        remindTime = (TextView) v.findViewById(R.id.remind_time);
        aSwitch = (Switch) v.findViewById(R.id.simpleSwitch);
        notImportant = (ImageView) v.findViewById(R.id.not_important);
        important = (ImageView) v.findViewById(R.id.important);

        pickerDialog = new TimePickerDialog(getContext());
        pickerDialog.btnSubmit.setOnClickListener(this);
        pickerDialog.btnCancel.setOnClickListener(this);
        remindTime.setOnClickListener(this);
        notImportant.setOnClickListener(this);
        important.setOnClickListener(this);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        //set the default time to be 2 hours later comparing current time.
        c.set(Calendar.HOUR, c.get(Calendar.HOUR) + 2);
        c.set(Calendar.MINUTE, 0);
        remindTime.setText(sdf.format(c.getTime()));

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    remindTime.setVisibility(View.VISIBLE);
                } else {
                    remindTime.setVisibility(View.GONE);
                }
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remind_time: {
                pickerDialog.show();
                break;
            }
            case R.id.btnSubmit: {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.YEAR, Integer.parseInt(pickerDialog.wheelDatePicker.year));
                c.set(Calendar.MONTH, Integer.parseInt(pickerDialog.wheelDatePicker.month) - 1);
                c.set(Calendar.DATE, Integer.parseInt(pickerDialog.wheelDatePicker.day) - 1);
                c.set(Calendar.HOUR, Integer.parseInt(pickerDialog.wheelTimePicker.hour));
                c.set(Calendar.MINUTE, Integer.parseInt(pickerDialog.wheelTimePicker.minute));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                remindTime.setText(sdf.format(c.getTime()));
                pickerDialog.dismiss();
                break;
            }
            case R.id.btnCancel: {
                pickerDialog.dismiss();
                break;
            }
            case R.id.important: {
                important.setVisibility(View.GONE);
                notImportant.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.not_important: {
                notImportant.setVisibility(View.GONE);
                important.setVisibility(View.VISIBLE);
                break;
            }

        }
    }
}
