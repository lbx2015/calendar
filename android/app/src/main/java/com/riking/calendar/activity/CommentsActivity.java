package com.riking.calendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.adapter.CommentListAdapter;
import com.riking.calendar.adapter.QuestionListAdapter;
import com.riking.calendar.util.ZGoto;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class CommentsActivity extends AppCompatActivity { //Fragment 数组
    CommentListAdapter mAdapter;
    RecyclerView recyclerView;
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
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void initEvents() {
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
}
