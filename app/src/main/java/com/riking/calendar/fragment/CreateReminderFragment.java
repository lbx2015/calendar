package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class CreateReminderFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_reminder_fragment, container, false);
        return v;
    }

}