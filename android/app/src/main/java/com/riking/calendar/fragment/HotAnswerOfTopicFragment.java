package com.riking.calendar.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.view.SimpleDividerItemDecoration;
import com.riking.calendar.R;
import com.riking.calendar.activity.TopicActivity;
import com.riking.calendar.adapter.HotAnswerOfTopicAdapter;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithoutProgress;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TopicParams;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class HotAnswerOfTopicFragment extends Fragment {
    protected SwipeRefreshLayout swipeRefreshLayout;
    TopicActivity activity;
    View v;
    HotAnswerOfTopicAdapter mAdapter;
    private PullToLoadViewWithoutFloatButton mPullToLoadView;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;

    public static HotAnswerOfTopicFragment newInstance(TopicActivity activity) {
        HotAnswerOfTopicFragment fragment = new HotAnswerOfTopicFragment();
        fragment.activity = activity;
        return fragment;
    }

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
        mAdapter = new HotAnswerOfTopicAdapter(getContext());
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
            }, 2000);
        }
        final TopicParams params = new TopicParams();
        params.topicId = activity.topicId;
        //excellent answer
        params.optType = 1;
        params.pcount = 30;
        params.pindex = page;

        APIClient.getEssenceAnswer(params, new ZCallBackWithoutProgress<ResponseModel<List<QAnswerResult>>>() {
            @Override
            public void callBack(ResponseModel<List<QAnswerResult>> response) {
                mPullToLoadView.setComplete();

                List<QAnswerResult> list = response._data;
                MyLog.d("list size: " + list.size());
                if (list.size() < params.pcount) {
                    isHasLoadedAll = true;
                    if (list.size() == 0) {
                        ZToast.toastEmpty();
                        return;
                    }
                }
                isLoading = false;
                nextPage = page + 1;
                mAdapter.setData(list);
            }
        });
        /*new Handler().postDelayed(new Runnable() {
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
