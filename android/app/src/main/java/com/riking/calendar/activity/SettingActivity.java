package com.riking.calendar.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ldf.calendar.Const;
import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.login_out_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
            case R.id.login_out_button: {
                SharedPreferences preferences = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                finish();
            }
        }
    }
}
