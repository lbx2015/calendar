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
import com.riking.calendar.util.DownLoadApk;
import com.riking.calendar.util.NetStateReceiver;
import com.riking.calendar.util.Preference;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class LaunchActivity extends AppCompatActivity {

    Handler handler = new Handler();
    private AlertDialog.Builder mDialog;

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


//        APIClient.checkUpdate(new CheckCallBack() {
//            @Override
//            public void onSuccess(AppVersionResult updateInfo) {
//                Logger.d("zzw", "on Success");
//                forceUpdate(updateInfo);
//                //返回0当前为最新版本，返回1有版本更新，返回2需要强制更新
//                if (updateInfo.type.equals("2")) {
//
//                } else if (updateInfo.type.equals("1")) {
//                } else {
//
//                }
//            }
//
//            @Override
//            public void onError() {
//
//            }
//        });
//        NetStateReceiver.registerNetworkStateReceiver(this);//初始化网络监听
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

    public void forceUpdate(final AppVersionResult updateInfo) {
        mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle(BuildConfig.APPLICATION_ID + "又更新咯！");
        mDialog.setMessage(updateInfo.msg);
        mDialog.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DownLoadApk.download(LaunchActivity.this, updateInfo.APKUrl, updateInfo.msg);
            }
        }).setCancelable(false).create().show();
    }

    @Override
    protected void onDestroy() {
//        NetStateReceiver.unRegisterNetworkStateReceiver(this);
        super.onDestroy();
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
