package com.riking.calendar.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.interfeet.AdapterEmptyListener;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

/**
 * Created by zw.zhang on 2018/1/4.
 * with pagination function
 */

public abstract class ZActivity<T extends ZAdater> extends AppCompatActivity {
    public PullToLoadViewWithoutFloatButton mPullToLoadView;
    public boolean isLoading = false;
    public boolean isHasLoadedAll = false;
    public int nextPage;
    public RecyclerView mRecyclerView;
    public TextView activityTitle;
    public T mAdapter;
    public View emptyLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setLayout();
        init();
    }

    public void init() {
        getViews();
        setEvents();
    }

    public abstract T getAdapter();

    public void setEvents() {
        mAdapter = getAdapter();
        mAdapter.emptyListener = new AdapterEmptyListener() {
            @Override
            public void onEmptyChanged(boolean isEmpty) {
                if (isEmpty) {
                    emptyLayout.setVisibility(View.VISIBLE);
                    mPullToLoadView.setVisibility(View.GONE);
                } else {
                    emptyLayout.setVisibility(View.GONE);
                    mPullToLoadView.setVisibility(View.VISIBLE);
                }
            }
        };
        mRecyclerView = mPullToLoadView.getRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setAdapter(mAdapter);
        mPullToLoadView.isLoadMoreEnabled(true);
        mPullToLoadView.setPullCallback(new PullCallback() {
            @Override
            public void onLoadMore() {
                load(nextPage);
            }

            @Override
            public void onRefresh() {
                isHasLoadedAll = false;
                load(1);
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
        initEvents();
    }

    private void load(final int page) {
        isLoading = true;
        if (page > 1 && isHasLoadedAll) {
            ZToast.toast("没有数据了");
            return;
        }

        if (mPullToLoadView != null) {
            mPullToLoadView.mSwipeRefreshLayout.setRefreshing(true);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (mPullToLoadView != null && isLoading) {
//                        ZToast.toast("加载失败");
//                        mPullToLoadView.setComplete();
//                    }
//                }
//            }, 10000);
        }

        loadData(page);
    }

    public void getViews() {
        emptyLayout = findViewById(R.id.empty);
        activityTitle = findViewById(R.id.activity_title);
        mPullToLoadView = (PullToLoadViewWithoutFloatButton) findViewById(R.id.pullToLoadView);
        initViews();
    }

    public abstract void loadData(int page);

    public abstract void initViews();

    public abstract void initEvents();


    //override this method if u need change the layout
    public void setLayout() {
        setContentView(R.layout.activity_base);
    }
}
