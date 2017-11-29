package com.riking.calendar.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.adapter.CommentListAdapter;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class CommentsActivity extends AppCompatActivity { //Fragment 数组
    CommentListAdapter mAdapter;
    RecyclerView recyclerView;
    TextView publicButton;
    TextInputEditText writeComment;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        init();
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        publicButton = findViewById(R.id.public_button);
        writeComment = findViewById(R.id.write_comment);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void initEvents() {
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
                if (s.length() > 0) {
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
        new Handler().postDelayed(new Runnable() {
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
        }, 1);
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
