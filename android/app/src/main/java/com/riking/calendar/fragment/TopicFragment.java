package com.riking.calendar.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.view.SimpleDividerItemDecoration;
import com.riking.calendar.R;
import com.riking.calendar.adapter.HomeAdapter;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.listener.ZCallBackWithoutProgress;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.HomeParams;
import com.riking.calendar.pojo.server.TQuestionResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

import java.util.Date;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class TopicFragment extends Fragment {
    View v;
    HomeAdapter mAdapter;
    String reqTimeStamp;
    String lastItemTime;
    private PullToLoadViewWithoutFloatButton mPullToLoadView;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v != null) return v;
        v = inflater.inflate(R.layout.topic_fragment, container, false);
        init();
        return v;
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        mPullToLoadView = (PullToLoadViewWithoutFloatButton) v.findViewById(R.id.pullToLoadView);
    }

    private void initEvents() {
        RecyclerView mRecyclerView = mPullToLoadView.getRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);

        //adding custom divider
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        mAdapter = new HomeAdapter(getContext());
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
        HomeParams p = new HomeParams();
        isLoading = true;
        if (page == 1) {
            if (reqTimeStamp == null) {
                p.direct = "up";
            } else {
                p.direct = "down";
            }
            p.reqTimeStamp = reqTimeStamp;
        } else {
            p.direct = "up";
            p.reqTimeStamp = lastItemTime;
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
            }, 2000);
        }
        APIClient.findHomePageData(p, new ZCallBackWithoutProgress<ResponseModel<List<TQuestionResult>>>() {
            @Override
            public void callBack(ResponseModel<List<TQuestionResult>> response) {
                MyLog.d("findNewList mPullToLoadView.setComplete()");
                mPullToLoadView.setComplete();
                isLoading = false;
                nextPage = page + 1;
                List<TQuestionResult> news = response._data;
                if (news.size() == 0) {
                    Toast.makeText(getContext(), "没有数据了", Toast.LENGTH_SHORT).show();
                    return;
                }

                reqTimeStamp = DateUtil.date2String(news.get(0).createdTime, CONST.YYYYMMDDHHMMSSSSS);
                Date lastTime = news.get(news.size() - 1).createdTime;
                //adding last item create time is null protection
                if (lastTime == null && news.size() > 1) {
                    lastTime = news.get(news.size() - 2).createdTime;
                }
                lastItemTime = DateUtil.date2String(lastTime, CONST.YYYYMMDDHHMMSSSSS);

                if (page == 1) {
                    mAdapter.mList.addAll(0, news);
                    mAdapter.notifyItemRangeInserted(0, news.size());
                } else {
                    int size = mAdapter.mList.size();
                    mAdapter.mList.addAll(size, news);
                    mAdapter.notifyItemRangeInserted(size - 1, news.size());
                }

            }
        });
      /*  isLoading = true;
        mPullToLoadView.mSwipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToLoadView.setComplete();
                if (page > 3) {
                    Toast.makeText(getContext(), "没有更多数据了",
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
        }, 3000);*/
    }
}
