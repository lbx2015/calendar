package com.riking.calendar.fragment;

import android.app.Activity;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.SearchActivity;
import com.riking.calendar.adapter.SearchReportsAdapter;
import com.riking.calendar.interfeet.PerformInputSearch;
import com.riking.calendar.interfeet.SubscribeReport;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUserReportRel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.server.ReportResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZPreference;
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

public class SearchReportsFragment extends Fragment implements SubscribeReport<ReportResult>, PerformInputSearch {
    protected SwipeRefreshLayout swipeRefreshLayout;
    View v;
    SearchReportsAdapter mAdapter;
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
        //adding dividers.
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new SearchReportsAdapter(this);
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
/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPullToLoadView.setComplete();
                if (page > 3) {
                    Toast.makeText(getContext(), "没有更多数据了",
                            Toast.LENGTH_SHORT).show();
                    isHasLoadedAll = true;
                    return;
                }
                List<ReportFrequency> data = new ArrayList<>();
                for (int i = 0; i <= 15; i++) {
                    ReportFrequency frequency = new ReportFrequency("dfldlfjdklsf", "fldsjfkld", "dklfdla", "dfjla", "djfladj");
                    data.add(frequency);
                }
                mAdapter.setData(data);
                isLoading = false;
                nextPage = page + 1;
            }
        }, 1000)*/
        search(searchCondition);
    }

    public void orderReport(ReportResult report) {
        Activity ac = getActivity();
        if (ac instanceof SearchActivity) {
            SearchActivity searchActivity = (SearchActivity) ac;
            searchActivity.saveToRealm();
        }
        AppUserReportRel a = new AppUserReportRel();
        a.appUserId = ZPreference.pref.getString(CONST.USER_ID, "");
        a.reportId = report.reportId;
        a.reportName = report.title;
        a.type = "1";
        APIClient.updateUserReportRelById(a, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
    }

    public void unorderReport(ReportResult report) {
        Activity ac = getActivity();
        if (ac instanceof SearchActivity) {
            SearchActivity searchActivity = (SearchActivity) ac;
            searchActivity.saveToRealm();
        }
        AppUserReportRel a = new AppUserReportRel();
        a.appUserId = ZPreference.pref.getString(CONST.USER_ID, "");
        a.reportId = report.reportId;
        a.reportName = report.title;
        a.type = "0";

        APIClient.updateUserReportRelById(a, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
    }

    @Override
    public boolean isAddedToMyOrder(ReportResult report) {
        return false;
    }

    @Override
    public boolean isInEditMode() {
        return true;
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
        //search reports
        params.objType = 1;
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
                //adding null protection
                if (r == null) {
                    return;
                }

                try {
                    String sourceString = r.source().readUtf8();
                    Gson s = new Gson();
                    JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                    String _data = jsonObject.get("_data").toString();

                    MyLog.d("_data " + _data);
                    //do nothing when the data is empty.
                    if (TextUtils.isEmpty(_data.trim())) {
                        return;
                    }
                    TypeToken<ResponseModel<List<ReportResult>>> token = new TypeToken<ResponseModel<List<ReportResult>>>() {};

                    ResponseModel<List<ReportResult>> responseModel = s.fromJson(sourceString, token.getType());
                    if (mPullToLoadView != null) {
                        mPullToLoadView.setComplete();
                    }

                    List<ReportResult> list = responseModel._data;

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
