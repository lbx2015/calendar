package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        StatusBarUtil.setTranslucent(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginWithPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginNavigateActivity.this, InputCellPhoneNumberActivity.class));
            }
        });


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);// support zoom
        webSettings.setUseWideViewPort(true);// 这个很关键
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        //
        webSettings.setDisplayZoomControls(false);
//        String gifPath = "http://pic48.nipic.com/file/20140912/7487939_223919315000_2.jpg";
//        String gifPath = "http://img.zcool.cn/community/018bcf575ed5200000018c1b264854.gif";

        String gifPath = "file:///android_asset/kingfisher.jpg";
        String videoPath = "file:///android_asset/LaunchTour.mp4";

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        String data = "<HTML><div align=\"center\" margin=\"0px\"><IMG  width=\"100%\" height=\"100%\" src=\"" + gifPath + "\" margin=\"0px\"/></Div>";//设置图片位于webview的中间位置

//        webView.loadDataWithBaseURL(gifPath, data, "text/html", "utf-8", null);

        String viedo = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <body style='margin:0'>\n" +
                "        <video   width=\"100%\" height=\"100%\"  autoplay loop>\n" +
                "              <source src=\"" + videoPath + "\" type=\"video/mp4\">\n" +
                "        </video>\n" +
                "    </body>\n" +
                "</html>";

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //loaded finished
               String js = "javascript: var v=document.getElementsByTagName('video')[0]; "
                        + "v.play(); ";

                //start play
                webView.loadUrl(js);
            }
        });
        webView.loadDataWithBaseURL(gifPath, viedo, "text/html", "utf-8", null);
        // 开启支持视频
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setGeolocationEnabled(true);
        String jsFullScreen = "javascript: var v=document.getElementsByTagName('video')[0]; " + "v.webkitEnterFullscreen(); ";
        webView.loadUrl(jsFullScreen);
//        webView.loadUrl(gifPath);
    }
}
