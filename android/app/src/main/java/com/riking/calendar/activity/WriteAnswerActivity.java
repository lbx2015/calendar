package com.riking.calendar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class WriteAnswerActivity extends AppCompatActivity {
    WebView webView;
    TextView publishButton;
    String questionId;
    String title;
    TextView questionTitle;

    public void clickCancel(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);
        questionId = getIntent().getStringExtra(CONST.QUESTION_ID);
        title = getIntent().getStringExtra(CONST.QUESTION_TITLE);
        init();
    }

    void init() {
        initViews();
        initEvents();
    }

    public void onClickPublish(View v) {
        webView.loadUrl("javascript:answerPublish()");
    }

    private void initEvents() {
        questionTitle.setText(title);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WriteAnswerActivity.this, "成功", Toast.LENGTH_SHORT).show();
            }
        });

        publishButton.setTextColor(ZR.getColor(R.color.color_489dfff));
        publishButton.setEnabled(true);

        TQuestionParams params = new TQuestionParams();
        params.tqId = questionId;
        APIClient.getAnswerEditHtml(params, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                webView.loadUrl(response._data);
            }
        });

        /*textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    publishButton.setTextColor(ZR.getColor(R.color.color_489dfff));
                    publishButton.setEnabled(true);
                } else {
                    publishButton.setTextColor(ZR.getColor(R.color.color_cccccc));
                    publishButton.setEnabled(false);
                }
            }
        });*/
    }

    private void initViews() {
        questionTitle = findViewById(R.id.question_title);
        publishButton = findViewById(R.id.publish_button);
        webView = findViewById(R.id.web_view);

        WebSettings settings = webView.getSettings();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
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
        webView.setWebViewClient(new WebViewClient() {

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

}
