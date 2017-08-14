package com.riking.calendar.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.activity.LoginActivity;
import com.riking.calendar.activity.SettingActivity;
import com.riking.calendar.activity.UserInfoActivity;
import com.riking.calendar.session.SessionManager;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class FourthFragment extends Fragment implements OnClickListener {
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fourth_fragment, container, false);
        v.findViewById(R.id.my_photo_layout).setOnClickListener(this);
        v.findViewById(R.id.set_layout).setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_photo_layout: {
                sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(Const.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
                if (sharedPreferences.getBoolean(SessionManager.IS_LOGIN, false)) {
                    startActivity(new Intent(getContext(), UserInfoActivity.class));
                } else {
                    startActivity((new Intent(getContext(), LoginActivity.class)));
                }
                break;
            }
            case R.id.set_layout: {
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            }
        }
    }
}
