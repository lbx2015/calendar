package com.riking.calendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.NetStateReceiver;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class LaunchActivity extends AppCompatActivity {
    private static final int sleepTime = 2000;
    Handler handler = new Handler();

    private void checkLoginState() {

        new Thread(new Runnable() {
            public void run() {

            /*    if (DemoHelper.getInstance().isLoggedIn()) {
                    // auto login mode, make sure all group and conversation is loaed before enter the main screen
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    long costTime = System.currentTimeMillis() - start;
                    //wait
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //enter main screen
                    startActivity(new Intent(LaunchActivity.this, ViewPagerActivity.class));
                    finish();
                } else {*/
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }

                if (ZPreference.pref.getBoolean(CONST.NEED_WELCOME_ACTIVITY, true)) {
                    ZGoto.to(WelcomeActivity.class);
                } else {
                    if (ZPreference.isLogin()) {
                        ZGoto.to(ViewPagerActivity.class);
                    } else {
                        ZGoto.toLoginActivity();
                    }
                }

//                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                finish();
//                }
            }
        }).start();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        checkLoginState();

        //if the user is not login
        if (!ZPreference.pref.getBoolean(CONST.IS_LOGIN, false)) {
            APIClient.getAllReports();
        } else {
            //get reminders and tasks of user from server
//            APIClient.synchAll();
        }
//        APIClient.getWorkDays();

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
