package com.riking.calendar.activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.NewsParams;
import com.riking.calendar.pojo.server.News;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ExpandCollapseExtention;
import com.riking.calendar.util.FileUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.widget.dialog.ShareBottomDialog;

import java.io.File;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class NewsDetailActivity extends AppCompatActivity { //Fragment 数组
    String newsId;
    TextView commentNumberTv;
    News news;
    //    RecyclerView suggestionQuestionsRecyclerView;
//    RecyclerView reviewsRecyclerView;
    private WebView webView;
    private LinearLayout bottomBar;
    private AppBarLayout appBarLayout;
    private TextView collectTv;
    private ImageView favoriteIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_activity);
        newsId = getIntent().getStringExtra(CONST.NEWS_ID);
        init();

    }

    public void clickBack(final View view) {
        onBackPressed();
    }

    void initEvents() {
        NewsParams p = new NewsParams();
        p.newsId = newsId;
        APIClient.getNewsDetail(p, new ZCallBack<ResponseModel<News>>() {
            @Override
            public void callBack(ResponseModel<News> response) {
                news = response._data;

                if (news.content.startsWith("http")) {
                    webView.loadUrl(news.content);
                } else {
                    webView.loadData(news.content, "text/html; charset=utf-8", "UTF-8");
                }
                commentNumberTv.setText(ZR.getNumberString(news.commentNumber));
                if (news.isCollect == 0) {
                    favoriteIv.setImageDrawable(ZR.getDrawable(R.drawable.com_toolbar_icon_collect_n));
                } else {
                    favoriteIv.setImageDrawable(ZR.getDrawable(R.drawable.com_toolbar_icon_collect_p));
                }
            }
        });
    }

    void init() {
        collectTv = findViewById(R.id.collect_tv);
        favoriteIv = findViewById(R.id.favorite_iv);
        commentNumberTv = findViewById(R.id.comment_number_tv);
        appBarLayout = findViewById(R.id.appbar);
        bottomBar = findViewById(R.id.bottom_bar);
        webView = findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptEnabled(true);
        enableWVCache();
        if (!isNetworkAvailable()) { // loading offline
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
//        webView.loadUrl("file:///android_asset/example.html");
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
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int oldVerticalOffset;

            boolean animating;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                MyLog.d("oldVerticalOffset - verticalOffset: " + (oldVerticalOffset - verticalOffset));
              /*  if (animating) {
                    return;
                }*/
                if (oldVerticalOffset - verticalOffset > 0) {
                /*    if (bottomBar.getVisibility() == View.GONE) {
                        return;
                    }*/
                    animating = true;
                    // Prepare the View for the animation
                    //scroll up
                    ExpandCollapseExtention.collapse(bottomBar);

                } else if (oldVerticalOffset - verticalOffset < 0) {
                   /* if (bottomBar.getVisibility() == View.VISIBLE) {
                        return;
                    }*/
                    animating = true;
                    //scroll down
                    ExpandCollapseExtention.expand(bottomBar);
                }
                //re-initiated the oldVerticalOffset
                oldVerticalOffset = verticalOffset;
                MyLog.d("verticalOffset: " + verticalOffset + "visibility: " + (bottomBar.getVisibility() == View.VISIBLE));
            }
        });

        initEvents();
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

    public void clickComments(final View view) {
        Intent i = new Intent(this, CommentsActivity.class);
        i.putExtra(CONST.NEWS_ID, newsId);
        i.putExtra(CONST.COMMENT_NUM, news.commentNumber);
        ZGoto.to(i);
    }

    public void clickFavorite(final View v) {
        final NewsParams params = new NewsParams();
        params.newsId = newsId;
        params.userId = ZPreference.getUserId();
        if (news.isCollect == 1) {
            params.enabled = 0;
        } else {
            params.enabled = 1;
        }
        APIClient.newsCollect(params, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                if (params.enabled == 1) {
                    news.isCollect = 1;
                    collectTv.setText("已收藏");
                    favoriteIv.setImageDrawable(ZR.getDrawable(R.drawable.com_toolbar_icon_collect_p));
                } else {
                    collectTv.setText("收藏");
                    news.isCollect = 0;
                    favoriteIv.setImageDrawable(ZR.getDrawable(R.drawable.com_toolbar_icon_collect_n));
                }
            }
        });
    }

    public void clickShare(final View v) {
        ShareBottomDialog dialog = new ShareBottomDialog(v.getContext());
        dialog.show();
    }

    class WebAppInterface {

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showImage(String imageUrl) {
            Toast.makeText(getBaseContext(), "showImage clicked." + imageUrl, Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(AnswerActivity.this, LookImageActivity.class);
//            i.putExtra(CONST.IMAGE_URL, imageUrl);
            // 预览单张图片
            startActivity(BGAPhotoPreviewActivity.newIntent(NewsDetailActivity.this, FileUtil.getImageSaveDir(), imageUrl));
//            startActivity(i);
        }
    }
}
