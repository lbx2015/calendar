package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.util.ZGoto;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class LoginNavigateActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_navigation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        StatusBarUtil.setTransparent(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    public void clickLoginWithPhone(View v) {
        ZGoto.to(InputCellPhoneNumberActivity.class);
//        finish();
    }

    public void clickPolicy(View v) {
        MyLog.d("click policy:");
        APIClient.getPolicyHtml(new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                Intent i = new Intent(LoginNavigateActivity.this, WebviewActivity.class);
                i.putExtra(CONST.WEB_URL, response._data);
                startActivity(i);
            }
        });
    }
}
