package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.ldf.calendar.Const;
import com.riking.calendar.jiguang.Logger;

import java.lang.reflect.Method;

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
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webview.getSettings().setJavaScriptEnabled(true);
        Class<?> clazz = webview.getSettings().getClass();
        try {
            Method method = clazz.getMethod(
                    "setAllowUniversalAccessFromFileURLs", boolean.class);
            if (method != null) {
                method.invoke(webview.getSettings(), true);
            }
        } catch (Exception e) {
            Logger.d("zzw", e.getMessage());
        }
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setContentView(webview);
    }
}
