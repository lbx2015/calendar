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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.SearchActivity;
import com.riking.calendar.adapter.ReportsOrderAdapter;
import com.riking.calendar.interfeet.SubscribeReport;
import com.riking.calendar.listener.PullCallback;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUserReportRel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.ReportFrequency;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.view.PullToLoadViewWithoutFloatButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class SearchReportsFragment extends Fragment implements SubscribeReport {
    protected SwipeRefreshLayout swipeRefreshLayout;
    View v;
    ReportsOrderAdapter mAdapter;
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
        mAdapter = new ReportsOrderAdapter(this);
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
                List<ReportFrequency> data = new ArrayList<>();
                for (int i = 0; i <= 15; i++) {
                    ReportFrequency frequency = new ReportFrequency("dfldlfjdklsf", "fldsjfkld", "dklfdla", "dfjla", "djfladj");
                    data.add(frequency);
                }
                mAdapter.setData(data);
                isLoading = false;
                nextPage = page + 1;
            }
        }, 1000);
    }


    public void orderReport(ReportFrequency report) {
        Activity ac = getActivity();
        if (ac instanceof SearchActivity) {
            SearchActivity searchActivity = (SearchActivity) ac;
            searchActivity.saveToRealm();
        }
        AppUserReportRel a = new AppUserReportRel();
        a.appUserId = ZPreference.pref.getString(CONST.USER_ID, "");
        a.reportId = report.reportId;
        a.reportName = report.reportName;
        a.type = "1";
        APIClient.updateUserReportRelById(a, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
    }

    public void unorderReport(ReportFrequency report) {
        Activity ac = getActivity();
        if (ac instanceof SearchActivity) {
            SearchActivity searchActivity = (SearchActivity) ac;
            searchActivity.saveToRealm();
        }
        AppUserReportRel a = new AppUserReportRel();
        a.appUserId = ZPreference.pref.getString(CONST.USER_ID, "");
        a.reportId = report.reportId;
        a.reportName = report.reportName;
        a.type = "0";

        APIClient.updateUserReportRelById(a, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
    }

    @Override
    public boolean isAddedToMyOrder(ReportFrequency report) {
        return false;
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }


}
