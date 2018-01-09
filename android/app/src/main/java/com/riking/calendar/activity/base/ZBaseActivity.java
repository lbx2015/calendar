package com.riking.calendar.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.util.ZToast;

import java.util.List;

/**
 * Created by zw.zhang on 2018/1/4.
 * with pagination function
 */

public abstract class ZBaseActivity<T extends ZAdater> extends AppCompatActivity {
    public SwipeRefreshLayout swipeRefreshLayout;
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
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                boolean isEmpty = mAdapter.mList == null || mAdapter.mList.size() == 0;
                MyLog.d("is empty: " + isEmpty);
                if (isEmpty) {
                    emptyLayout.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                } else {
                    emptyLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isHasLoadedAll = false;
                load(1);
            }
        });


        load(1);
        initEvents();
    }

    //without pagination function
    public void setData2Adapter(List<?> list) {
        setData2Adapter(1, list);
    }

    public void setData2Adapter(int currentPage, List<?> list) {
        isLoading = false;
        swipeRefreshLayout.setRefreshing(false);
        if (list != null && list.size() == 0) {
            ZToast.toast("没有更多数据了");
            emptyLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
            return;
        }

        emptyLayout.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

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

        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
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
        mRecyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
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
