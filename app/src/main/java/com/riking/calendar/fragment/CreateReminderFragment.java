package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.widget.WheelPopWindow;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class CreateReminderFragment extends Fragment implements View.OnClickListener {

    private WheelPopWindow popWindow;
    private View selectRemindTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_reminder_fragment, container, false);
        popWindow = new WheelPopWindow(getContext());
        selectRemindTime = v.findViewById(R.id.select_remind_time);
        selectRemindTime.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_remind_time: {
                popWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
                break;
            }
        }
    }
}
