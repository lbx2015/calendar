package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

import com.ldf.calendar.Const;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class ReportDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        WebView webview = new WebView(this);
        Bundle bundle = getIntent().getExtras();
        webview.loadUrl(bundle.getString(Const.REPORT_URL, null));
        setContentView(webview);
    }
}
