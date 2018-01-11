package com.riking.calendar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.QuestionListAdapter;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.server.TopicQuestion;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class QuestionActivity extends AppCompatActivity {
    public View followButton;
    public TextView followTv;
    QuestionListAdapter mAdapter;
    RecyclerView recyclerView;
    TopicQuestion question;
    //set web view
    WebView webview;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;
    private TextView questionTitleTv;
    private TextView followNumberTv;
    private TextView answerNumberTv;
    private TextView letMeAnswerTv;
    private String questionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_activity);
        questionId = getIntent().getStringExtra(CONST.QUESTION_ID);
        init();
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        webview = (WebView) findViewById(R.id.web_view);
        letMeAnswerTv = findViewById(R.id.let_me_answer);
        answerNumberTv = findViewById(R.id.answer_number);
        followButton = findViewById(R.id.follow_button);
        followTv = findViewById(R.id.follow_text);
        questionTitleTv = findViewById(R.id.question_title);
        followNumberTv = findViewById(R.id.follow_numbers);
        recyclerView = findViewById(R.id.recycler_view);

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
        settings.setSupportZoom(false);
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

    private void initEvents() {
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        mAdapter = new QuestionListAdapter(this);
        recyclerView.setAdapter(mAdapter);

        letMeAnswerTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                clickLetMeAnswer(v);
            }
        });
        followButton.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                if (question == null) {
                    ZToast.toast("数据加载失败");
                    return;
                }

                final TQuestionParams params = new TQuestionParams();
                params.attentObjId = question.topicQuestionId;
                //question
                params.objType = 1;
                //followed
                if (question.isFollow == 1) {
                    params.enabled = 0;
                } else {
                    params.enabled = 1;
                }

                APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        question.isFollow = params.enabled;
                        if (question.isFollow == 1) {
                            ZToast.toast("关注成功");
                        } else {
                            ZToast.toast("取消关注");
                        }
                        updateFollowButton();
                    }
                });
            }
        });
        loadData(1);
    }

    private void setData() {
        long startTime = System.currentTimeMillis();
        updateFollowButton();
        MyLog.d("updateFollowButton used time: " + (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
        //set follow number
        followNumberTv.setText(question.followNum + "人关注");
        //set question title
        questionTitleTv.setText(question.title);
        //set answer number
        answerNumberTv.setText("" + question.answerNum);
        mAdapter.addAll(question.questionAnswers);
        MyLog.d("used time: " + (System.currentTimeMillis() - startTime));

        if (question.content.startsWith("http")) {
            webview.loadUrl(question.content);
        } else {
            webview.loadData(question.content, "text/html; charset=utf-8", "UTF-8");
        }
    }

    private void updateFollowButton() {
        //followed
        if (question.isFollow == 1) {
            followTv.setText("已关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            followTv.setTextColor(ZR.getColor(R.color.color_999999));
            followButton.setBackground(ZR.getDrawable(R.drawable.follow_border_gray));
        } else {
            followTv.setText("关注");
            followTv.setTextColor(ZR.getColor(R.color.white));
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus_w, 0, 0, 0);
            followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            followButton.setBackground(ZR.getDrawable(R.drawable.follow_button_border));
        }
    }

    private void loadData(final int page) {
        isLoading = true;
        TQuestionParams params = new TQuestionParams();
        params.tqId = questionId;
        APIClient.getTopicQuestion(params, new ZCallBack<ResponseModel<TopicQuestion>>() {
            @Override
            public void callBack(ResponseModel<TopicQuestion> response) {
                question = response._data;
                setData();
                isLoading = false;
                nextPage = page + 1;
            }
        });

    /*    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page > 3) {
                    Toast.makeText(QuestionActivity.this, "没有更多数据了",
                            Toast.LENGTH_SHORT).show();
                    isHasLoadedAll = true;
                    return;
                }
                for (int i = 0; i <= 15; i++) {
                    mAdapter.add(i + "");
                }

                isLoading = false;
                nextPage = page + 1;
            }
        }, 1);*/
    }

    public void clickBack(final View view) {
        onBackPressed();
    }

    public void clickInvitePerson(final View view) {
        Intent i = new Intent(this, InvitePersonActivity.class);
        i.putExtra(CONST.QUESTION_ID, questionId);
        ZGoto.to(i);
    }

    public void clickLetMeAnswer(final View view) {
        Intent i = new Intent(this, WriteAnswerActivity.class);
        i.putExtra(CONST.QUESTION_ID, question.topicQuestionId);
        i.putExtra(CONST.QUESTION_TITLE, question.title);
        ZGoto.to(i);
    }
}
