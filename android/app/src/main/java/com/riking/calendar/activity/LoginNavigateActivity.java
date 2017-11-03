package com.riking.calendar.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.util.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class LoginNavigateActivity extends AppCompatActivity {

    @BindView(R.id.phone_login)
    TextView loginWithPhone;
    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_navigation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        StatusBarUtil.setTranslucent(this);
    }

    @Override
    protected void onStart() {
        ButterKnife.bind(this);
        super.onStart();
        loginWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginNavigateActivity.this, InputCellPhoneNumberActivity.class));
            }
        });
        String gifPath = "http://g.hiphotos.baidu.com/image/h%3D300/sign=8e17c4cca6c3793162688029dbc6b784/a1ec08fa513d2697af090e975cfbb2fb4216d826.jpg";
//        String gifPath = "http://img.zcool.cn/community/018bcf575ed5200000018c1b264854.gif";
//        String gifPath = "file:///android_asset/timg.gif";
        webView.loadUrl(gifPath);
    }
}
