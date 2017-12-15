package com.riking.calendar.activity;

import android.content.Context;
import android.content.Intent;
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

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.CommentListAdapter;
import com.riking.calendar.adapter.ReplyListAdapter;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.CommentParams;
import com.riking.calendar.pojo.params.NewsParams;
import com.riking.calendar.pojo.server.NCReply;
import com.riking.calendar.pojo.server.NewsComment;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.KeyboardEditText;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/24.
 * news comments page
 */

public class CommentsActivity extends AppCompatActivity { //Fragment 数组
    CommentListAdapter mAdapter;
    RecyclerView recyclerView;
    TextView publicButton;
    KeyboardEditText writeComment;
    ImageView answerIcon;
    boolean isOpened = false;
    //0 reply news ,1 rely comment,2 reply reply
    int commentFlag = 0;
    NewsComment replyComment;
    NCReply replyReply;
    String commentContent;
    ReplyListAdapter replyListAdapter;
    RecyclerView replyRecyclerView;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;
    private String newsId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Intent i = getIntent();
        newsId = i.getStringExtra(CONST.NEWS_ID);
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
        setPublicClickListener();
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
                    commentContent = s.toString().trim();
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

    private void setPublicClickListener() {
        publicButton.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {

                if (replyComment != null || replyReply != null) {
                    CommentParams params = new CommentParams();
                    params.content = commentContent;
                    params.objType = 2;
                    if (replyComment != null) {
                        params.commentId = replyComment.newsCommentId;
                        //reset to null
                        replyComment = null;

                    } else if (replyReply != null) {
                        params.toUserId = replyReply.fromUser.userId;
                        params.commentId = replyReply.commentId;
                        //reset to null
                        replyReply = null;
                    }

                    MyLog.d("commentReply:" + params.toString());

                    APIClient.commentReply(params, new ZCallBackWithFail<ResponseModel<NCReply>>() {
                        @Override
                        public void callBack(ResponseModel<NCReply> response) throws Exception {
                            MyLog.d("comment reply : " + failed + " replyRecyclerView visibility: " + (replyRecyclerView.getVisibility() == View.VISIBLE));
                            if (failed) {

                            } else {
                                writeComment.setText("");
                                if (replyListAdapter.mList.size() > 0) {
                                    MyLog.d("reply list adapter mlist size before : " + replyListAdapter.mList.size());
                                    replyListAdapter.mList.add(0, response._data);
                                    MyLog.d("reply list adapter mlist size after : " + replyListAdapter.mList.size());
                                    replyListAdapter.notifyItemInserted(0);
                                } else {
                                    loadData(1);
                                }
//                                replyListAdapter.notifyDataSetChanged();
//                                replyRecyclerView.scrollToPosition(0);
                                Toast.makeText(CommentsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                //reply news
                else {
                    NewsParams p = new NewsParams();
                    p.newsId = newsId;
                    p.content = commentContent;
                    p.userId = ZPreference.pref.getString(CONST.USER_ID, "");
                    APIClient.newsCommentPub(p, new ZCallBackWithFail<ResponseModel<NewsComment>>() {
                        @Override
                        public void callBack(ResponseModel<NewsComment> response) {
                            if (failed) {

                            } else {
                                writeComment.setText("");
                                mAdapter.mList.add(0, response._data);
                                mAdapter.notifyItemInserted(0);
                                recyclerView.scrollToPosition(0);
                                Toast.makeText(CommentsActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void loadData(final int page) {
        isLoading = true;
        loadNews(page);
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

    private void loadNews(final int page) {
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
    }

    public void clickBack(final View view) {
        onBackPressed();
    }

    public void clickWriteComment(NewsComment c, ReplyListAdapter adapter, RecyclerView replyRecyclerView) {
        this.replyRecyclerView = replyRecyclerView;
        replyListAdapter = adapter;
        commentFlag = 1;
        replyComment = c;
        showKeyBoard(c.userName);
    }

    public void clickReply(final NCReply reply, ReplyListAdapter adapter, RecyclerView replyRecyclerView) {
        this.replyRecyclerView = replyRecyclerView;
        replyListAdapter = adapter;
        commentFlag = 2;
        replyReply = reply;

        showKeyBoard(reply.fromUser.userName);
    }

    public void showKeyBoard(String userName) {
        writeComment.setHint("回复" + userName);
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
