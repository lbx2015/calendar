package com.riking.calendar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.util.CONST;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SubmitQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_activity);
        //set web view
        WebView webview = (WebView) findViewById(R.id.web_view);

        Bundle bundle = getIntent().getExtras();
        webview.loadUrl(bundle.getString(CONST.WEB_URL, null));

        WebSettings settings = webview.getSettings();
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        settings.setBuiltInZoomControls(false); // 设置支持缩放
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(true);
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
        webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hit = view.getHitTestResult();
                Logger.d("zzw", "url string: " + url + " hit type: " + hit.getType());

                //open pdf in third party browsers
                if (hit != null && (!url.endsWith("html"))) {
                    int hitType = hit.getType();
                    if (hitType == WebView.HitTestResult.SRC_ANCHOR_TYPE
                            || hitType == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {// 点击超链接
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else if (url.startsWith("mailto:")) {
                        //Handle mail Urls
                        startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
                    } else if (url.startsWith("tel:")) {
                        //Handle telephony Urls
                        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                    } else {
                        view.loadUrl(url);
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                if (uri.toString().startsWith("mailto:")) {
                    //Handle mail Urls
                    startActivity(new Intent(Intent.ACTION_SENDTO, uri));
                } else if (uri.toString().startsWith("tel:")) {
                    //Handle telephony Urls
                    startActivity(new Intent(Intent.ACTION_DIAL, uri));
                } else {
                    //Handle Web Urls
                    view.loadUrl(uri.toString());
                }
                return true;
            }
        });
    }

    public void onBackClick(View v) {
        onBackPressed();
    }
}
