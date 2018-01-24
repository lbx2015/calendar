package com.riking.calendar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.SearchActivity;
import com.riking.calendar.adapter.SearchReportsAdapter;
import com.riking.calendar.fragment.base.ZFragment;
import com.riking.calendar.interfeet.PerformInputSearch;
import com.riking.calendar.interfeet.SubscribeReport;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.params.SubscribeReportParam;
import com.riking.calendar.pojo.server.ReportResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZR;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class SearchReportsFragment extends ZFragment<SearchReportsAdapter> implements SubscribeReport<ReportResult>, PerformInputSearch {
    String searchCondition;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public SearchReportsAdapter getAdapter() {
        return new SearchReportsAdapter(this);
    }

    public void initViews() {
    }

    public void initEvents() {
        ZR.setImage(emptyIv, R.drawable.default_icon_nosearch);
        emptyTv.setText("无搜索结果");
        //adding dividers.
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    public void loadData(final int page) {
        search(searchCondition);
    }

    public void orderReport(ReportResult report) {
        Activity ac = getActivity();
        if (ac instanceof SearchActivity) {
            SearchActivity searchActivity = (SearchActivity) ac;
            searchActivity.saveToRealm();
        }
        SubscribeReportParam a = new SubscribeReportParam();
        a.reportIds = report.reportId;
        a.subscribeType = 1;
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
        SubscribeReportParam a = new SubscribeReportParam();
        a.reportIds = report.reportId;
        a.subscribeType = 0;

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
                setComplete();
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
                    TypeToken<ResponseModel<List<ReportResult>>> token = new TypeToken<ResponseModel<List<ReportResult>>>() {
                    };

                    ResponseModel<List<ReportResult>> responseModel = s.fromJson(sourceString, token.getType());
                    if (mPullToLoadView != null) {
                        mPullToLoadView.setComplete();
                    }

                    List<ReportResult> list = responseModel._data;

                    setData2Adapter(1, list);
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
