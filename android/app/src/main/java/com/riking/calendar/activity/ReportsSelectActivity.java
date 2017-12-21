package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.InterestingReportAdapter;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.AppUserRecommend;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SubscribeReportParam;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.view.InterestingReportLinearLayout;
import com.riking.calendar.view.ZFlowLayout;

import java.util.ArrayList;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class ReportsSelectActivity extends AppCompatActivity {
    InterestingReportAdapter mAdapter;
    RecyclerView mRecyclerView;
    ZFlowLayout zFlowLayout;
//    private SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<String> selectedReportIds = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reports_selection);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //set translucent background for the status bar
        StatusBarUtil.setTranslucent(this);
        init();
    }

    private void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        zFlowLayout = findViewById(R.id.flow_layout);
//        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.userId.swipeRefreshLayout);
    }

    public void onClickStart(View view) {
        if (selectedReportIds.size() > 0) {
            SubscribeReportParam appUserReportResult = new SubscribeReportParam();
            StringBuilder sb = new StringBuilder();
            for (String r : selectedReportIds) {
                sb.append(r);
                if (selectedReportIds.lastIndexOf(r) < selectedReportIds.size() - 1) {
                    sb.append(",");
                }
            }

            appUserReportResult.reportIds = sb.toString();
            APIClient.userAddReportEdit(appUserReportResult, new ZCallBackWithFail<ResponseModel<Short>>() {
                @Override
                public void callBack(ResponseModel<Short> response) {
                    if (failed) {
                    } else {
                        startActivity(new Intent(ReportsSelectActivity.this, ViewPagerActivity.class));

                    }
                }
            });
        }
    }

    private void initEvents() {
//        mRecyclerView = findViewById(R.userId.recyclerView);
        //two columns
//        GridLayoutManager manager = new GridLayoutManager(this, 2);
//
//        mRecyclerView.setLayoutManager(manager);
//        mAdapter = new InterestingReportAdapter(this);
//        mRecyclerView.setAdapter(mAdapter);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                loadData(1);
//            }
//        });
//        swipeRefreshLayout.setRefreshing(true);
        loadData(1);
    }

    private void loadData(final int page) {
        APIClient.getAllReports(new ZCallBackWithFail<ResponseModel<ArrayList<AppUserRecommend>>>() {
            @Override
            public void callBack(ResponseModel<ArrayList<AppUserRecommend>> response) {
                if (failed) {
//                mAdapter.mList.clear();
                    //clear the recycled view pool
//                mRecyclerView.getRecycledViewPool().clear();
//                mAdapter.mList = response._data;
//                mAdapter.notifyDataSetChanged();
                } else {
                    ArrayList<AppUserRecommend> reports = response._data;
                    if (reports != null) {
                        for (final AppUserRecommend r : reports) {
                            //inflate the item view from layout xml
                            final InterestingReportLinearLayout root = (InterestingReportLinearLayout) LayoutInflater.from(ReportsSelectActivity.this).inflate(R.layout.intersting_report_item, null);
                            //get view
                            final TextView reportNameTV = root.findViewById(R.id.interesting_report);
                            final ImageView checkImage = root.findViewById(R.id.check_image);
                            //set data
                            reportNameTV.setText(r.reportName);

                            root.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (root.checked) {
                                        //the report is not been subscribed
                                        root.checked = false;
                                        //remove the report userId form the result
                                        selectedReportIds.remove(r.reportId);
                                        checkImage.setImageDrawable(checkImage.getResources().getDrawable(R.drawable.login_icon_add));
                                        reportNameTV.setTextColor(reportNameTV.getResources().getColor(R.color.color_222222));
                                        root.setBackground(root.getResources().getDrawable(R.drawable.not_selected__interesting_reports_background));
                                    } else {
                                        //the report is been subscribed.
                                        root.checked = true;
                                        //add the report userId into the result
                                        selectedReportIds.add(r.reportId);

                                        checkImage.setImageDrawable(checkImage.getResources().getDrawable(R.drawable.login_icon_dh));
                                        reportNameTV.setTextColor(reportNameTV.getResources().getColor(R.color.white));
                                        root.setBackground(root.getResources().getDrawable(R.drawable.selected__interesting_reports_background));
                                    }
                                }
                            });

                            zFlowLayout.addView(root);
                        }
                    }
                }
            }
        });
//                swipeRefreshLayout.setRefreshing(false);

    }
}

