package com.riking.calendar.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.riking.calendar.R;
import com.riking.calendar.adapter.SearchNewsAdapter;
import com.riking.calendar.interfeet.PerformInputSearch;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.server.NewsResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class SearchNewsFragment extends Fragment implements PerformInputSearch {
    protected SwipeRefreshLayout swipeRefreshLayout;
    View v;
    SearchNewsAdapter mAdapter;
    String searchCondition;
    private PullToLoadViewWithoutFloatButton mPullToLoadView;
    private boolean isLoading = false;
    private boolean isHasLoadedAll = false;
    private int nextPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (v != null) return v;
        v = inflater.inflate(R.layout.search_fragment, container, false);
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
        //adding dividers.
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SearchNewsAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        mPullToLoadView.isLoadMoreEnabled(false);
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
        search(searchCondition);
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
        }, 1000);*/
    }

    @Override
    public void search(String searchCondition) {
        if (mPullToLoadView != null) {
            mPullToLoadView.mSwipeRefreshLayout.setRefreshing(true);
        }

        if (TextUtils.isEmpty(searchCondition)) {
            return;
        }
        //keep the the search condition in order to refresh the page
        this.searchCondition = searchCondition;

        SearchParams params = new SearchParams();
        params.keyWord = searchCondition;
        //search topics
        params.objType = 4;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mPullToLoadView != null) {
                    mPullToLoadView.setComplete();
                }
            }
        }, 2000);

        APIClient.findSearchList(params, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (mPullToLoadView != null) {
                    mPullToLoadView.setComplete();
                }

                ResponseBody r = response.body();
                try {
                    String sourceString = r.source().readUtf8();
                    Gson s =  new GsonBuilder().setDateFormat(CONST.YYYYMMDDHHMMSSSSS).create();;

                    TypeToken<ResponseModel<List<NewsResult>>> token = new TypeToken<ResponseModel<List<NewsResult>>>() {
                    };

                    ResponseModel<List<NewsResult>> responseModel = s.fromJson(sourceString, token.getType());
                    if (mPullToLoadView != null) {
                        mPullToLoadView.setComplete();
                    }

                    List<NewsResult> list = responseModel._data;

                    if (list.isEmpty()) {
                        Toast.makeText(getContext(), "没有更多数据了",
                                Toast.LENGTH_SHORT).show();
                        isHasLoadedAll = true;
                        return;
                    }

                    mAdapter.setData(list);
                    isLoading = false;
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}
