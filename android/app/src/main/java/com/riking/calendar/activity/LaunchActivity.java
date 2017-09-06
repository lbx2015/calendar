package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.ldf.calendar.Const;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.Preference;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class LaunchActivity extends AppCompatActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        ImageView view = new ImageView(this);
        setContentView(view);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Preference.pref.getBoolean(Const.NEED_WELCOME_ACTIVITY, true)) {
                    //Welcome activity only need once
                    Preference.put(Const.NEED_WELCOME_ACTIVITY, false);
                    Intent i = new Intent(LaunchActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(LaunchActivity.this, ViewPagerActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2000);

        //if the user is not login
        if (!Preference.pref.getBoolean(Const.IS_LOGIN, false)) {
            APIClient.getAllReports();
        } else {
            //get reminders and tasks of user from server
            APIClient.synchAll();
        }
        APIClient.getWorkDays();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(LaunchActivity.this, WelcomeActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
