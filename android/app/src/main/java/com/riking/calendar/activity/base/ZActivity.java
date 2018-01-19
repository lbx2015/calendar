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
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

import java.util.List;

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
    RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mAdapter != null && emptyLayout != null) {
                if (mAdapter.getItemCount() == 0) {
                    emptyLayout.setVisibility(View.VISIBLE);
                    mPullToLoadView.setVisibility(View.GONE);
                } else {
                    emptyLayout.setVisibility(View.GONE);
                    mPullToLoadView.setVisibility(View.VISIBLE);
                    mPullToLoadView.invalidate();
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("zzw", this + "on create");
        super.onCreate(savedInstanceState);
        setLayout();
        init();
    }

    final void init() {
        getViews();
        setEvents();
    }

    public abstract T getAdapter();

    public void setEvents() {
        mAdapter = getAdapter();
        mRecyclerView = mPullToLoadView.getRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(dataObserver);
        dataObserver.onChanged();
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

    //without pagination function
    public void setData2Adapter(List<?> list) {
        setData2Adapter(1, list);
    }

    public void setData2Adapter(int currentPage, List<?> list) {
        isLoading = false;
        mPullToLoadView.setComplete();
        if (list != null && list.size() == 0) {
            ZToast.toast("没有更多数据了");
            emptyLayout.setVisibility(View.VISIBLE);
            mPullToLoadView.setVisibility(View.GONE);
            return;
        }

        emptyLayout.setVisibility(View.GONE);
        mPullToLoadView.setVisibility(View.VISIBLE);

        //first page
        if (currentPage == 1) {
            mAdapter.setData(list);
        } else {
            mAdapter.addAllAtEnd(list);
        }
        nextPage = currentPage + 1;
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

    public abstract void loadData(final int page);

    public abstract void initViews();

    public abstract void initEvents();


    //override this method if u need change the layout
    public void setLayout() {
        setContentView(R.layout.activity_base);
    }

    public void clickBack(final View view) {
        onBackPressed();
    }
}
