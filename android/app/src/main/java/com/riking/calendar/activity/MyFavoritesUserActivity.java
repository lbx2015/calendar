package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.MyFollowersAdapter;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserFollowParams;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/24.
 * answer comments page
 */

public class MyFavoritesUserActivity extends AppCompatActivity { //Fragment 数组
    MyFollowersAdapter mAdapter;
    RecyclerView recyclerView;
    String userId;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;
    private PullToLoadViewWithoutFloatButton mPullToLoadView;
    private TextView activityTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_followers);

        Intent i = getIntent();
        userId = i.getStringExtra(CONST.USER_ID);
        init();
        activityTitle.setText("我关注的人");
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        mPullToLoadView = (PullToLoadViewWithoutFloatButton) findViewById(R.id.pullToLoadView);
        activityTitle = findViewById(R.id.activity_title);
        recyclerView = mPullToLoadView.getRecyclerView();
    }

    private void initEvents() {
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        mAdapter = new MyFollowersAdapter();
        recyclerView.setAdapter(mAdapter);
        mPullToLoadView.isLoadMoreEnabled(true);
        mPullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                loadData(nextPage);
            }

            @Override
            public void onRefresh() {
                isHasLoadedAll = false;
                loadData(1);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return isHasLoadedAll;
            }
        });

        mPullToLoadView.initLoad();
    }

    private void loadData(final int page) {
        isLoading = true;
        if (page > 1 && isHasLoadedAll) {
            return;
        }
        if (mPullToLoadView != null) {
            mPullToLoadView.mSwipeRefreshLayout.setRefreshing(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mPullToLoadView != null) {
                        mPullToLoadView.setComplete();
                    }
                }
            }, 5000);
        }

        final UserFollowParams params = new UserFollowParams();
        //person type
        params.objType = 3;
        params.pindex = page;
        params.userId = userId;
        APIClient.getMyFavoriateUsers(params, new ZCallBackWithFail<ResponseModel<List<AppUserResult>>>() {
            @Override
            public void callBack(ResponseModel<List<AppUserResult>> response) {
                mPullToLoadView.setComplete();
                isLoading = false;
                if (!failed) {
                    List<AppUserResult> list = response._data;
                    MyLog.d("list size: " + list.size());
                    if (list.size() < params.pcount) {
                        isHasLoadedAll = true;
                        if (list.size() == 0) {
                            ZToast.toastEmpty();
                            return;
                        }
                    }

                    nextPage = page + 1;
                    mAdapter.setData(list);
                }
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


}
