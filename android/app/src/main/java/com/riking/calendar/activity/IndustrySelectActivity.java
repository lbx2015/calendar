package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.adapter.IndustryAdapter;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class IndustrySelectActivity extends AppCompatActivity {
    IndustryAdapter mAdapter;
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
        startActivity(new Intent(this, PositionSelectActivity.class));
    }


    private void initEvents() {
        RecyclerView mRecyclerView = mPullToLoadView.getRecyclerView();
        //adding default divider
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToLoadView.setComplete();
                if (page > 3) {
                    Toast.makeText(IndustrySelectActivity.this, "没有更多数据了",
                            Toast.LENGTH_SHORT).show();
                    isHasLoadedAll = true;
                    return;
                }
//                int startPosition = mAdapter.getItemCount() - 1;
                for (int i = 0; i <= 1; i++) {
                    mAdapter.mList.add(i + "");
                }
//                mAdapter.notifyItemRangeInserted(startPosition, 1);
                mAdapter.notifyDataSetChanged();
                isLoading = false;
                nextPage = page + 1;
            }
        }, 1000);
    }
}

