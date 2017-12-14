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
import com.riking.calendar.adapter.AnswerCommentListAdapter;
import com.riking.calendar.adapter.AnswerReplyListAdapter;
import com.riking.calendar.adapter.MyRepliesAdapter;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.CommentParams;
import com.riking.calendar.pojo.params.QAnswerParams;
import com.riking.calendar.pojo.server.NCReply;
import com.riking.calendar.pojo.server.QAComment;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.KeyboardEditText;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/24.
 * answer comments page
 */

public class MyRepliesActivity extends AppCompatActivity { //Fragment 数组
    MyRepliesAdapter mAdapter;
    RecyclerView recyclerView;
    QAComment replyComment;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_replies);
        Intent i = getIntent();
        init();
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void initEvents() {
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        mAdapter = new MyRepliesAdapter();
        recyclerView.setAdapter(mAdapter);
        loadData(1);
    }

    private void loadData(final int page) {
        isLoading = true;
        loadAnswer(page);
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

    private void loadAnswer(final int page) {
     /*   QAnswerParams params = new QAnswerParams();
        APIClient.qACommentList(params, new ZCallBack<ResponseModel<List<QAComment>>>() {
            @Override
            public void callBack(ResponseModel<List<QAComment>> response) {
                List<QAComment> comments = response._data;
                isLoading = false;
                if (comments.size() == 0) {
                    ZToast.toast("没有更多数据了");
                    return;
                }
                mAdapter.addAll(comments);
                nextPage = page + 1;
            }
        });*/
    }

    public void clickBack(final View view) {
        onBackPressed();
    }



}
