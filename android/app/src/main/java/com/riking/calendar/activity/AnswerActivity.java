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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.QAnswerParams;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.server.QuestionAnswer;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ExpandCollapseExtention;
import com.riking.calendar.util.FileUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.widget.dialog.ShareBottomDialog;

import java.io.File;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class AnswerActivity extends AppCompatActivity { //Fragment 数组
    public View followButton;
    public TextView followTv;
    String answerId;
    QuestionAnswer answer;
    //    RecyclerView suggestionQuestionsRecyclerView;
//    RecyclerView reviewsRecyclerView;
    private WebView webView;
    private LinearLayout bottomBar;
    private AppBarLayout appBarLayout;
    private TextView shareTv;
    private TextView collectTv;
    private TextView agreeTv;
    private TextView commentsTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answer_detail_activity);
        answerId = getIntent().getStringExtra(CONST.ANSWER_ID);
        init();
        getAnswerInfo();

    }

    public void clickBack(final View view) {
        onBackPressed();
    }

    void init() {
        followButton = findViewById(R.id.follow_button);
        followTv = findViewById(R.id.follow_text);
        commentsTv = findViewById(R.id.comments_tv);
        agreeTv = findViewById(R.id.agree_tv);
        collectTv = findViewById(R.id.collect_tv);
        shareTv = findViewById(R.id.share_tv);
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

        //set follow user click listener
        setFollowClickListener();
       /* //init recycler view
        suggestionQuestionsRecyclerView = findViewById(R.userId.suggestion_questions_recyclerview);
        suggestionQuestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        suggestionQuestionsRecyclerView.setAdapter(new QuestionsAdapter(this));
        //init reviews recycler view
        reviewsRecyclerView = findViewById(R.userId.review_recyclerview);
        reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewsRecyclerView.setAdapter(new ReviewsAdapter(this));*/
    }

    private void setFollowClickListener() {
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TQuestionParams params = new TQuestionParams();
                params.attentObjId = answer.userId;
                //follow person
                params.objType = 3;
                //followed
                if (answer.isFollow == 1) {
                    params.enabled = 0;
                } else {
                    params.enabled = 1;
                }

                APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        answer.isFollow = params.enabled;
                        if (answer.isFollow == 1) {
                            ZToast.toast("关注成功");
                        } else {
                            ZToast.toast("取消关注");
                        }
                        showInvited();
                    }
                });
            }
        });
    }

    private void showInvited() {
        if (answer.isFollow == 0) {
            followTv.setText("关注");
            followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
            followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border));
        } else {
            followTv.setText("已关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            followTv.setTextColor(ZR.getColor(R.color.color_999999));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border_gray));
        }
    }

    private void getAnswerInfo() {
        QAnswerParams params = new QAnswerParams();
        params.questAnswerId = answerId;
        APIClient.getAnswerInfo(params, new ZCallBack<ResponseModel<QuestionAnswer>>() {
            @Override
            public void callBack(ResponseModel<QuestionAnswer> response) {
                answer = response._data;
                commentsTv.setText(ZR.getNumberString(answer.commentNum));
                agreeTv.setText(ZR.getNumberString(answer.agreeNum));

                if (answer.isAgree == 1) {
                    agreeTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.com_icon_zan_p, 0, 0);
                    agreeTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                } else {
                    agreeTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.com_icon_zan_n, 0, 0);
                    agreeTv.setTextColor(ZR.getColor(R.color.color_999999));
                }

                if (answer.isCollect == 1) {
                    collectTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.com_toolbar_icon_collect_p, 0, 0);
                    collectTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                } else {
                    collectTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.com_toolbar_icon_collect_n, 0, 0);
                    collectTv.setTextColor(ZR.getColor(R.color.color_999999));
                }
                //update the follow button
                showInvited();
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
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        webView.addJavascriptInterface(new WebAppInterface(), "Android");
    }

    public void clickComments(final View view) {
        Intent i = new Intent(this, AnswerCommentsActivity.class);
        i.putExtra(CONST.ANSWER_ID, answer.questionAnswerId);
        ZGoto.to(i);
    }

    public void clickAgree(final View view) {
        agreeTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final QAnswerParams p = new QAnswerParams();
                p.questAnswerId = answer.questionAnswerId;
                //answer agree type
                p.optType = 1;
                if (answer.isAgree == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }
                APIClient.qAnswerAgree(p, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        answer.isAgree = p.enabled;
                        if (p.enabled == 1) {
                            answer.agreeNum = answer.agreeNum + 1;
                            agreeTv.setText(ZR.getNumberString(answer.agreeNum));
                            agreeTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                            agreeTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.com_icon_zan_p, 0, 0);
                            Toast.makeText(agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            answer.agreeNum = answer.agreeNum - 1;
                            agreeTv.setText(ZR.getNumberString(answer.agreeNum));
                            agreeTv.setTextColor(ZR.getColor(R.color.color_999999));
                            ZToast.toast("取消点赞");
                            agreeTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.com_icon_zan_n, 0, 0);
                        }
                    }
                });
            }
        });
    }

    public void clickFavorite(final View v) {
        collectTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final QAnswerParams p = new QAnswerParams();
                p.questAnswerId = answer.questionAnswerId;
                //answer collect type
                p.optType = 2;
                if (answer.isCollect == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }
                APIClient.qAnswerAgree(p, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        answer.isCollect = p.enabled;
                        if (p.enabled == 1) {
                            collectTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                            collectTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.com_toolbar_icon_collect_p, 0, 0);
                            Toast.makeText(agreeTv.getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            collectTv.setTextColor(ZR.getColor(R.color.color_999999));
                            ZToast.toast("取消收藏");
                            collectTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.com_toolbar_icon_collect_n, 0, 0);
                        }
                    }
                });
            }
        });

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
