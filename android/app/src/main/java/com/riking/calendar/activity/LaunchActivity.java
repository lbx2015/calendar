package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.ldf.calendar.Const;
import com.riking.calendar.BuildConfig;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.CheckCallBack;
import com.riking.calendar.pojo.AppVersionResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.AppInnerDownLoder;
import com.riking.calendar.util.DownLoadApk;
import com.riking.calendar.util.NetStateReceiver;
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
//        NetStateReceiver.unRegisterNetworkStateReceiver(this);
//        NetStateReceiver.registerNetworkStateReceiver(this);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Preference.pref.getBoolean(Const.NEED_WELCOME_ACTIVITY, true)) {
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

        //register observer
        NetStateReceiver.registerObserver(new NetStateReceiver.NetChangeObserver() {
            @Override
            public void onNetConnected() {
                Toast.makeText(getApplicationContext(), "connected", Toast.LENGTH_SHORT).show();
                Logger.d("zzw", "on NetConnected.");
                APIClient.updatePendingUpdates();
            }

            @Override
            public void onNetDisConnect() {
                Toast.makeText(getApplicationContext(), "disconnected.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
//        NetStateReceiver.unRegisterNetworkStateReceiver(this);
        super.onDestroy();
    }
}
