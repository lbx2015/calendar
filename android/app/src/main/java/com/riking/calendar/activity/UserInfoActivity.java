package com.riking.calendar.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.ldf.calendar.Const;
import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView userName;
    public TextView email;
    public TextView department;
    SharedPreferences preference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_user_info);
        findViewById(R.id.back).setOnClickListener(this);
        userName = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        department = (TextView) findViewById(R.id.depart);
        userName.setText(preference.getString(Const.USER_NAME, null));
        email.setText(preference.getString(Const.USER_EMAIL, null));
        department.setText(preference.getString(Const.USER_DEPT, null));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
            }
        }
    }
}
