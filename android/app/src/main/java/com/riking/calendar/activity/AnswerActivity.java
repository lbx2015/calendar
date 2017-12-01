package com.riking.calendar.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.util.FileUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.widget.dialog.ShareBottomDialog;

import java.io.File;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class AnswerActivity extends AppCompatActivity { //Fragment 数组
    //    RecyclerView suggestionQuestionsRecyclerView;
//    RecyclerView reviewsRecyclerView;
    private WebView webView;
    private LinearLayout bottomBar;
    private AppBarLayout appBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_detail_activity);
        init();

    }

    public void clickBack(final View view) {
        onBackPressed();
    }

    void init() {
        appBarLayout = findViewById(R.id.appbar);
        bottomBar = findViewById(R.id.bottom_bar);
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
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int oldVerticalOffset;

            boolean animating;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                MyLog.d("oldVerticalOffset - verticalOffset: " + (oldVerticalOffset - verticalOffset));
                if (animating) {
                    return;
                }
                if (oldVerticalOffset - verticalOffset > 0) {
                    if (bottomBar.getVisibility() == View.GONE) {
                        return;
                    }
                    animating = true;
                    // Prepare the View for the animation
                    //scroll up
                    bottomBar.animate()
                            .translationY(bottomBar.getHeight())
                            .alpha(0.f)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    animating = false;
                                    // superfluous restoration
                                    bottomBar.setVisibility(View.GONE);
                                    bottomBar.setAlpha(0.f);
                                    bottomBar.setTranslationY(0.f);
                                }
                            });

                } else if (oldVerticalOffset - verticalOffset < 0) {
                    if (bottomBar.getVisibility() == View.VISIBLE) {
                        return;
                    }
                    animating = true;
                    //scroll down
//                    bottomBar.setVisibility(View.VISIBLE);
                    bottomBar.setAlpha(0.f);
                    bottomBar.animate().translationY(0).alpha(1.f).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animating = false;
                            bottomBar.setVisibility(View.VISIBLE);
                            bottomBar.setAlpha(1.f);
                        }
                    });
                    ;
                }
                //re-initiated the oldVerticalOffset
                oldVerticalOffset = verticalOffset;
                MyLog.d("verticalOffset: " + verticalOffset);
            }
        });
       /* //init recycler view
        suggestionQuestionsRecyclerView = findViewById(R.id.suggestion_questions_recyclerview);
        suggestionQuestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        suggestionQuestionsRecyclerView.setAdapter(new QuestionsAdapter(this));
        //init reviews recycler view
        reviewsRecyclerView = findViewById(R.id.review_recyclerview);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewsRecyclerView.setAdapter(new ReviewsAdapter(this));*/
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
        ZGoto.to(this, CommentsActivity.class);
    }

    public void clickAgree(final View view) {

    }

    public void clickFavorite(final View v) {

    }

    public void clickShare(final View v) {
        new ShareBottomDialog(v.getContext()).show();
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
            startActivity(BGAPhotoPreviewActivity.newIntent(AnswerActivity.this, FileUtil.getImageSaveDir(), imageUrl));
//            startActivity(i);
        }
    }
}
