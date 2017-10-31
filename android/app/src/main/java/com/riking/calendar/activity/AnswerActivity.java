package com.riking.calendar.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.FileUtil;

import java.io.File;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class AnswerActivity extends AppCompatActivity { //Fragment 数组
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_detail_activity);
        init();

    }

    void init() {
        webView = findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        enableWVCache();
        if (!isNetworkAvailable()) { // loading offline
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        webView.loadUrl("file:///android_asset/example.html");
//        webView.loadUrl("http://image.baidu.com/search/index?ct=201326592&cl=2&st=-1&lm=-1&nc=1&ie=utf-8&tn=baiduimage&ipn=r&rps=1&pv=&fm=rs1&word=%E7%BE%8E%E5%A5%B3%E5%9B%BE%E7%89%87&oriquery=%E5%9B%BE%E7%89%87&ofr=%E5%9B%BE%E7%89%87&sensitive=0");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void enableWVCache() {

        webView.getSettings().setDomStorageEnabled(true);

        // Set cache size to 8 mb by default. should be more than enough
        webView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);

        File dir = getCacheDir();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        webView.getSettings().setAppCachePath(dir.getPath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);

        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.addJavascriptInterface(new WebAppInterface(), "Android");
    }

    class WebAppInterface {


        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showImage(String imageUrl) {
            Toast.makeText(getBaseContext(), "showImage clicked."+imageUrl, Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(AnswerActivity.this, LookImageActivity.class);
//            i.putExtra(CONST.IMAGE_URL, imageUrl);
            // 预览单张图片
            startActivity(BGAPhotoPreviewActivity.newIntent(AnswerActivity.this, FileUtil.getImageSaveDir(), imageUrl));
//            startActivity(i);
        }
    }
}
