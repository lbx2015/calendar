package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.necer.ncalendar.view.SimpleDividerItemDecoration;
import com.riking.calendar.R;
import com.riking.calendar.adapter.NewsAdapter;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.NewsParams;
import com.riking.calendar.pojo.server.News;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class FinanceNewsFragment extends Fragment {
    View v;
    NewsAdapter mAdapter;
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
        v = inflater.inflate(R.layout.finances_news, container, false);
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
        mAdapter = new NewsAdapter();
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
        NewsParams p = new NewsParams();
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
        APIClient.findNewsList(p, new ZCallBack<ResponseModel<List<News>>>() {
            @Override
            public void callBack(ResponseModel<List<News>> response) {
                mPullToLoadView.setComplete();
                isLoading = false;
                nextPage = page + 1;
                List<News> news = response._data;
                if (news.size() == 0) {
                    Toast.makeText(getContext(), "没有数据了", Toast.LENGTH_SHORT).show();
                    return;
                }

                reqTimeStamp = DateUtil.date2String(news.get(0).createdTime, CONST.YYYYMMDDHHMMSSSSS);
                lastItemTime = DateUtil.date2String(news.get(news.size() - 1).createdTime, CONST.YYYYMMDDHHMMSSSSS);

                if (page == 0) {
                    mAdapter.mList.addAll(0, news);
                    mAdapter.notifyItemRangeInserted(0, news.size());
                } else {
                    mAdapter.mList.addAll((mAdapter.mList.size()), news);
                    mAdapter.notifyItemRangeInserted(mAdapter.mList.size() - 1, news.size());
                }

            }
        });
       /* new Handler().postDelayed(new Runnable() {
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
