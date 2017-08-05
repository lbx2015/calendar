package com.riking.calendar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.UserInfoActivity;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class FourthFragment extends Fragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fourth_fragment, container, false);
        v.findViewById(R.id.my_photo_layout).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_photo_layout: {
                startActivity(new Intent(getContext(), UserInfoActivity.class));
                break;
            }
        }
    }
}
