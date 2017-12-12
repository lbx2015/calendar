package com.riking.calendar.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.SearchTopicAdapter;
import com.riking.calendar.interfeet.PerformInputSearch;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.server.TopicResult;
import com.riking.calendar.retrofit.APIClient;
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

public class SearchTopicFragment extends Fragment implements PerformInputSearch {
    protected SwipeRefreshLayout swipeRefreshLayout;
    View v;
    SearchTopicAdapter mAdapter;
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
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new SearchTopicAdapter(getContext());
        mRecyclerView.setAdapter(mAdapter);
        //no pagination
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
    }

    @Override
    public void search(String searchCondition) {
        this.searchCondition = searchCondition;

        if (mPullToLoadView != null) {
            mPullToLoadView.mSwipeRefreshLayout.setRefreshing(true);
        }
        if (TextUtils.isEmpty(searchCondition)) {
            return;
        }

        SearchParams params = new SearchParams();
        params.keyWord = searchCondition;
        //search topics
        params.objType = 2;
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
                ResponseBody r = response.body();
                try {
                    String sourceString = r.source().readUtf8();
                    if (TextUtils.isEmpty(sourceString.trim())) {
                        return;
                    }
                    Gson s = new Gson();
                    JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                    String _data = jsonObject.get("_data").toString();

                    MyLog.d("_data topic " + _data);
                    //do nothing when the data is empty.
                    if (TextUtils.isEmpty(_data.trim())) {
                        return;
                    }

                    TypeToken<ResponseModel<List<TopicResult>>> token = new TypeToken<ResponseModel<List<TopicResult>>>() {
                    };

                    ResponseModel<List<TopicResult>> responseModel = s.fromJson(sourceString, token.getType());
                    if (mPullToLoadView != null) {
                        mPullToLoadView.setComplete();
                    }

                    List<TopicResult> list = responseModel._data;

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
