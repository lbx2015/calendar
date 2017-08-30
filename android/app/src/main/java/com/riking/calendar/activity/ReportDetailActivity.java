package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ldf.calendar.Const;
import com.riking.calendar.jiguang.Logger;

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
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.d("zzw", "url : " + url);
                view.loadUrl(url);
                return true;
            }
        });
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        setContentView(webview);
    }
}
