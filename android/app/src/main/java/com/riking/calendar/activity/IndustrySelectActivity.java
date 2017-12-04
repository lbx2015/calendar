package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.riking.calendar.R;
import com.riking.calendar.adapter.IndustryAdapter;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.Industry;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

import java.util.ArrayList;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class IndustrySelectActivity extends AppCompatActivity {
    IndustryAdapter mAdapter;
    RecyclerView mRecyclerView;
    private PullToLoadViewWithoutFloatButton mPullToLoadView;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_industry_selection);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //set translucent background for the status bar
        StatusBarUtil.setTranslucent(this);
        init();
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        mPullToLoadView = (PullToLoadViewWithoutFloatButton) findViewById(R.id.pullToLoadViewWithoutFloatButton);
    }

    public void onClickNextStep(View view) {
        AppUser result = new AppUser();
        result.isGuide = "1";
        result.userId = (Preference.pref.getString(CONST.USER_ID, ""));
        APIClient.updateUserInfo(result, new ZCallBackWithFail<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
        startActivity(new Intent(this, ViewPagerActivity.class));
    }

    private void initEvents() {
        mRecyclerView = mPullToLoadView.getRecyclerView();
        //adding default divider
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new IndustryAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
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

        //load data from server
        APIClient.getIndustries(new ZCallBackWithFail<ResponseModel<ArrayList<Industry>>>() {
            @Override
            public void callBack(ResponseModel<ArrayList<Industry>> response) {
                mPullToLoadView.setComplete();
                mAdapter.mList.clear();
                //clear the recycled view pool
                mRecyclerView.getRecycledViewPool().clear();
                mAdapter.mList = response._data;
                mAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        });
    }
}

