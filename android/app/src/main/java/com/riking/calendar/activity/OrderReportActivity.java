package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.ReportFrequencyAdapter;
import com.riking.calendar.adapter.ReportsOrderAdapter;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.ReportAgence;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.view.OrderReportFrameLayout;
import com.riking.calendar.view.ZReportFlowLayout;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class OrderReportActivity extends AppCompatActivity {
    public ZReportFlowLayout zReportFlowLayout;
    public TextView button;
    public boolean cancelOrder = false;
    public RecyclerView reportFrequencyRecyclerView;
    public RecyclerView reportsRecyclerViews;
    public ReportFrequencyAdapter reportFrequencyAdapter = new ReportFrequencyAdapter(this);
    public ReportsOrderAdapter reportsOrderAdapter = new ReportsOrderAdapter(this);
    public List<ReportAgence> reportAgences;
    public int orgonizeType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);
        init();
        /*//change the status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        StatusBarUtil.setTranslucent(this);*/
        for (int i = 0; i < 5; i++) {
            //inflate the item view from layout xml
            final OrderReportFrameLayout root = (OrderReportFrameLayout) LayoutInflater.from(this).inflate(R.layout.my_order_report_item, null);
            root.init();
            //set data
            root.reportNameTV.setText("G010" + i);
            root.checkImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zReportFlowLayout.removeView(root);
                }
            });
            zReportFlowLayout.addView(root);
        }
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        zReportFlowLayout = findViewById(R.id.flow_layout);
        button = findViewById(R.id.button);
        reportFrequencyRecyclerView = findViewById(R.id.report_frequency_recycler_view);
        reportsRecyclerViews = findViewById(R.id.report_recycler_view);
    }

    private void initEvents() {
        setRecyclerView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancelOrder) {
                    cancelOrder = false;
                    button.setText("编辑");
                } else {
                    button.setText("保存");
                    cancelOrder = true;
                }
                int size = zReportFlowLayout.getChildCount();
                for (int i = 0; i < size; i++) {
                    OrderReportFrameLayout f = (OrderReportFrameLayout) zReportFlowLayout.getChildAt(i);
                    if (f.checkImage.getVisibility() == View.VISIBLE) {
                        f.checkImage.setVisibility(View.GONE);
                    } else {
                        f.checkImage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void setRecyclerView() {
        //set layout manager for the recycler view.
        reportFrequencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportsRecyclerViews.setLayoutManager(new LinearLayoutManager(this));
        //adding dividers.
        reportsRecyclerViews.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        reportFrequencyRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //set adapters
        reportFrequencyRecyclerView.setAdapter(reportFrequencyAdapter);
        reportsRecyclerViews.setAdapter(reportsOrderAdapter);

/*
        //Todo test only
        List<ReportAgence> reportAgences = new ArrayList<>();
        ReportAgence reportAgence = new ReportAgence();
        List<BaseModelPropdict> list = new ArrayList<>();
        BaseModelPropdict baseModelPropdict = new BaseModelPropdict("id", "key", "BASE 月");
        List<ReportFrequency> reportFrequencies = new ArrayList<>();
        ReportFrequency reportFrequency = new ReportFrequency("reportId", "G0100", "资产负债项目统计", "isComplete", "strFrency");
        reportFrequencies.add(reportFrequency);
        baseModelPropdict.list = reportFrequencies;
        list.add(baseModelPropdict);
        reportAgence.list = list;
        reportAgence.agenceName = "CBRC";
        reportAgences.add(reportAgence);

        reportFrequencyAdapter.setData(reportAgences.get(0).list);
        reportsOrderAdapter.setData(reportAgences.get(0).list.get(0).list);*/

        APIClient.getAllReports(new ZCallBackWithFail<ResponseModel<List<ReportAgence>>>() {
            @Override
            public void callBack(ResponseModel<List<ReportAgence>> response) {
                if (failed) {

                } else {
                    reportAgences = response._data;
                    MyLog.d("reportAgences: " + reportAgences);
                    reportFrequencyAdapter.setData(reportAgences.get(0).list);
                    reportsOrderAdapter.setData(reportAgences.get(0).list.get(0).list);
                }
            }
        });
    }
}
