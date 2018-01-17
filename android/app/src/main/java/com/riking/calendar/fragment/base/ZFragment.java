package com.riking.calendar.fragment.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

import java.util.List;

/**
 * with pagination function
 * Created by zw.zhang on 2017/12/22.
 */

public abstract class ZFragment<T extends ZAdater> extends Fragment {
    public PullToLoadViewWithoutFloatButton mPullToLoadView;
    public boolean isLoading = false;
    public boolean isHasLoadedAll = false;
    public int nextPage;
    public RecyclerView mRecyclerView;
    public T mAdapter;
    public View v;
    public SwipeRefreshLayout emptyLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v != null) return v;
        v = createView(inflater, container, savedInstanceState);
        if (v == null) {
            v = inflater.inflate(R.layout.z_fragment, container, false);
        }
        init();
        return v;
    }

    //should override this method
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    public void init() {
        getViews();
        setEvents();
    }

    public void getViews() {
        emptyLayout = v.findViewById(R.id.empty);
        mPullToLoadView = (PullToLoadViewWithoutFloatButton) v.findViewById(R.id.pullToLoadView);

        if (emptyLayout != null) {
            setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            emptyLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mPullToLoadView.initLoad();
                }
            });
        }
        initViews();
    }

    public void setColorSchemeResources(int... colorResIds) {
        emptyLayout.setColorSchemeResources(colorResIds);
    }

    private void updateEmpty(int currentPage, List<?> list) {
        isLoading = false;
        mPullToLoadView.setComplete();
        if (list.size() == 0) {
            ZToast.toast("没有更多数据了");
            emptyLayout.setVisibility(View.VISIBLE);
            mPullToLoadView.setVisibility(View.GONE);
            return;
        }

        emptyLayout.setVisibility(View.GONE);
        mPullToLoadView.setVisibility(View.VISIBLE);
        nextPage = currentPage + 1;
    }

    public void appendData2Adapter(int currentPage, List<?> list) {
        updateEmpty(currentPage, list);
        if (currentPage == 1) {
            mAdapter.addAllAtStart(list);
        } else {
            mAdapter.addAllAtEnd(list);
        }
    }

    public void setData2Adapter(int currentPage, List<?> list) {
        updateEmpty(currentPage, list);
        //first page
        if (currentPage == 1) {
            mAdapter.setData(list);
        } else {
            mAdapter.addAllAtEnd(list);
        }
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
                    ZR.show(emptyLayout);
                    ZR.hide(mPullToLoadView);
                } else {
                    ZR.hide(emptyLayout);
                    ZR.show(mPullToLoadView);
                }
            }
        });

        mRecyclerView = mPullToLoadView.getRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
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

    public abstract void loadData(int page);

    public abstract void initViews();

    public abstract void initEvents();
}
