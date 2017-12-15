package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.MyFollowersAdapter;
import com.riking.calendar.util.CONST;

/**
 * Created by zw.zhang on 2017/7/24.
 * answer comments page
 */

public class MyFollowersActivity extends AppCompatActivity { //Fragment 数组
    MyFollowersAdapter mAdapter;
    RecyclerView recyclerView;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;
    private TextView activityTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_followers);

        Intent i = getIntent();
        init();
        if (getIntent().getIntExtra(CONST.MY_FOLLOW, 0) == 1) {
            activityTitle.setText("我关注的人");
        }
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        activityTitle = findViewById(R.id.activity_title);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void initEvents() {
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        mAdapter = new MyFollowersAdapter();
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
      /*  QAnswerParams params = new QAnswerParams();
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
