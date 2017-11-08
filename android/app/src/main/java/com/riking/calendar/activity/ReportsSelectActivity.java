package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.riking.calendar.R;
import com.riking.calendar.adapter.InterestingReportAdapter;
import com.riking.calendar.util.StatusBarUtil;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class ReportsSelectActivity extends AppCompatActivity {
    InterestingReportAdapter mAdapter;
    RecyclerView mRecyclerView;
//    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reports_selection);
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
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    }

    public void onClickStart(View view) {
        startActivity(new Intent(this,ViewPagerActivity.class));
    }

    private void initEvents() {
        mRecyclerView = findViewById(R.id.recyclerView);
        //two columns
        GridLayoutManager manager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(manager);
        mAdapter = new InterestingReportAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadData(1);
//            }
//        });

//        swipeRefreshLayout.setRefreshing(true);
        loadData(1);
    }

    private void loadData(final int page) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                swipeRefreshLayout.setRefreshing(false);
                mAdapter.mList.clear();
                //clear the recycled view pool
                mRecyclerView.getRecycledViewPool().clear();
                for (int i = 0; i <= 3; i++) {
                    mAdapter.mList.add(i + "");
                }
                mAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }
}

