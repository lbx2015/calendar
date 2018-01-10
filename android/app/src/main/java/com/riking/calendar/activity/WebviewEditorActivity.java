package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.util.CONST;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class WebviewEditorActivity extends AppCompatActivity {
    TextView activityTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        activityTitle = findViewById(R.id.title);

        //set web view
        WebView webview = (WebView) findViewById(R.id.web_view);

        Bundle bundle = getIntent().getExtras();
//        webview.loadUrl(bundle.getString(CONST.WEB_URL, null));
        webview.loadUrl("file:///android_asset/example.html");
        String activityName = bundle.getString(CONST.ACTIVITY_NAME, "");
        if (activityName.equals("SettingActivity")) {
            //set activity title
            activityTitle.setText("关于我们");
        } else {
            activityTitle.setVisibility(View.GONE);
        }

        WebSettings settings = webview.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setDatabaseEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAppCacheEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
    }

    public void onBackClick(View v) {
        onBackPressed();
    }
}
