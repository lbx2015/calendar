package com.riking.calendar.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.adapter.CommentListAdapter;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.NewsParams;
import com.riking.calendar.pojo.server.NewsComment;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.KeyboardEditText;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class CommentsActivity extends AppCompatActivity { //Fragment 数组
    CommentListAdapter mAdapter;
    RecyclerView recyclerView;
    TextView publicButton;
    KeyboardEditText writeComment;
    ImageView answerIcon;
    boolean isOpened = false;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;
    private String newsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        newsId = getIntent().getStringExtra(CONST.NEWS_ID);
        init();
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        answerIcon = findViewById(R.id.icon_answer);
        publicButton = findViewById(R.id.public_button);
        writeComment = findViewById(R.id.write_comment);
        recyclerView = findViewById(R.id.recycler_view);
    }

    public void setListenerToRootView() {
        final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) writeComment.getLayoutParams();
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > 100) { // 99% of the time the height diff will be due to a keyboard.
                    //keyboard is up
                    params.leftMargin = (int) ZR.convertDpToPx(15);
                    answerIcon.setVisibility(View.GONE);
                    if (isOpened == false) {
                        //Do two things, make the view top visible and the editText smaller
                    }
                    isOpened = true;
                } else if (isOpened == true) {
                    //keyboard is down
                    params.leftMargin = (int) ZR.convertDpToPx(5);
                    writeComment.clearFocus();
                    answerIcon.setVisibility(View.VISIBLE);
                    isOpened = false;
                }
            }
        });
    }

    private void initEvents() {
        setListenerToRootView();
        writeComment.setOnKeyboardListener(new KeyboardEditText.KeyboardListener() {
            @Override
            public void onStateChanged(KeyboardEditText keyboardEditText, boolean showing) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) writeComment.getLayoutParams();
                if (showing) {
                    params.leftMargin = (int) ZR.convertDpToPx(15);
                    answerIcon.setVisibility(View.GONE);
                } else {
                    params.leftMargin = (int) ZR.convertDpToPx(5);
                    writeComment.clearFocus();
                    answerIcon.setVisibility(View.VISIBLE);
                }
            }
        });
        writeComment.setShowSoftInputOnFocus(true);
        writeComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() > 0) {
                    publicButton.setTextColor(ZR.getColor(R.color.color_489dfff));
                    publicButton.setEnabled(true);
                } else {
                    publicButton.setTextColor(ZR.getColor(R.color.color_cccccc));
                    publicButton.setEnabled(false);
                }
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        mAdapter = new CommentListAdapter(this);
        recyclerView.setAdapter(mAdapter);
        loadData(1);
    }

    private void loadData(final int page) {
        isLoading = true;
        NewsParams p = new NewsParams();
        p.newsId = newsId;

        APIClient.findNewsCommentList(p, new ZCallBack<ResponseModel<List<NewsComment>>>() {
            @Override
            public void callBack(ResponseModel<List<NewsComment>> response) {
                List<NewsComment> comments = response._data;
                isLoading = false;
                if (comments.size() == 0) {
                    Toast.makeText(CommentsActivity.this, "没有更多数据了",
                            Toast.LENGTH_SHORT).show();
                    isHasLoadedAll = true;
                    return;
                }
                mAdapter.addAll(comments);
                nextPage = page + 1;
            }
        });
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page > 3) {
                    Toast.makeText(CommentsActivity.this, "没有更多数据了",
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

    public void clickWriteComment(final String hintText) {
        writeComment.setHint("回复" + hintText);
        writeComment.performClick();
        writeComment.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(writeComment.getWindowToken(), 0);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                writeComment.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0));
                writeComment.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 0, 0, 0));
            }
        }, 200);
    }
}
