package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.riking.calendar.R;
import com.riking.calendar.adapter.TopicsAdapter;
import com.riking.calendar.interfeet.SubscribeReport;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.AppUserReportRel;
import com.riking.calendar.pojo.AppUserReportResult;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.ReportAgence;
import com.riking.calendar.pojo.server.ReportFrequency;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;
import com.riking.calendar.view.OrderReportFrameLayout;
import com.riking.calendar.view.ZReportFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class AddTopicActivity extends AppCompatActivity implements SubscribeReport {
    public ZReportFlowLayout zReportFlowLayout;
    public boolean editMode = false;
    public RecyclerView topicRecyclerView;
    public TopicsAdapter mAdapter;
    //user subscriber reports
    List<ReportFrequency> mySubscribedReports;
    FrameLayout myOrderLayout;
    TextInputEditText textInputEditText;

    public void clickBack(final View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        init();
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        textInputEditText = findViewById(R.id.search_topic);
        zReportFlowLayout = findViewById(R.id.flow_layout);
        topicRecyclerView = findViewById(R.id.recycler_view);
    }


    private void initEvents() {
        setRecyclerView();
    }


    private void setRecyclerView() {
        //set layout manager for the recycler view.
        topicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TopicsAdapter(this);
        //set adapters
        topicRecyclerView.setAdapter(mAdapter);
        loadReport();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateOrderReports();
    }

    private void updateOrderReports() {
        if (Preference.pref.getBoolean(CONST.ORDER_REPORTS_CHANGED, false)) {
            Preference.put(CONST.ORDER_REPORTS_CHANGED, false);

            Gson gson = new Gson();
            final ReportFrequency[] orderReports = gson.fromJson(Preference.pref.getString(CONST.ORDER_REPORTS, ""), ReportFrequency[].class);
            final ReportFrequency[] disOrderReports = gson.fromJson(Preference.pref.getString(CONST.DIS_ORDER_REPORTS, ""), ReportFrequency[].class);
            boolean reportAdded = false;
            if (orderReports != null) {
                for (int i = 0; i < orderReports.length; i++) {
                    if (mySubscribedReports == null) {
                        break;
                    }
                    reportAdded = false;

                    if (mySubscribedReports.contains(orderReports[i])) {
                        reportAdded = true;
                    }

                    //if not added to my subscribed, add it
                    if (!reportAdded) {
                        mySubscribedReports.add(orderReports[i]);
                    }

                }
            }

            if (disOrderReports != null) {
                for (int i = 0; i < disOrderReports.length; i++) {
                    if (mySubscribedReports == null) {
                        break;
                    }
                    mySubscribedReports.remove(disOrderReports[i]);
                }
            }
        }
        enterEditMode();
    }


    private void loadReport() {
        //request all reports
        AppUser u = new AppUser();
        //set user id
        u.id = Preference.pref.getString(CONST.USER_ID, "");
        APIClient.getAllReports(u, new ZCallBackWithFail<ResponseModel<List<ReportAgence>>>() {
            @Override
            public void callBack(ResponseModel<List<ReportAgence>> response) {
            }
        });

        APIClient.findUserReportList(u, new ZCallBackWithFail<ResponseModel<List<ReportFrequency>>>() {
            @Override
            public void callBack(ResponseModel<List<ReportFrequency>> response) {
                if (failed) {

                } else {
                    mySubscribedReports = response._data;
                    drawMyOrders();
                }
            }
        });
    }

    public void drawMyOrders() {
        //redraw the reports
        zReportFlowLayout.removeAllViews();
        if (mySubscribedReports.size() > 0) {
            myOrderLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mySubscribedReports.size(); i++) {
                final ReportFrequency appUserReportRel = mySubscribedReports.get(i);
                //inflate the item view from layout xml
                final OrderReportFrameLayout root = (OrderReportFrameLayout) LayoutInflater.from(AddTopicActivity.this).inflate(R.layout.my_order_report_item, null);
                root.init();
                //set data
                root.reportNameTV.setText(appUserReportRel.reportName);
                root.checkImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zReportFlowLayout.removeView(root);
                        mySubscribedReports.remove(appUserReportRel);
                        if (mySubscribedReports.size() == 0) {
                            //hide the title
                            myOrderLayout.setVisibility(View.GONE);
                        }
                    }
                });
                zReportFlowLayout.addView(root);
            }
        } else {
            //hide the title of the orders
            myOrderLayout.setVisibility(View.GONE);
        }
    }

    public void orderReport(ReportFrequency report) {
        AppUserReportRel a = new AppUserReportRel();
        a.appUserId = Preference.pref.getString(CONST.USER_ID, "");
        a.reportId = report.reportId;
        a.reportName = report.reportName;
        mySubscribedReports.add(report);
        enterEditMode();
    }

    public void unorderReport(ReportFrequency report) {
        for (ReportFrequency r : mySubscribedReports) {
            if (r.reportId.equals(report.reportId)) {
                mySubscribedReports.remove(r);
                break;
            }
        }
        enterEditMode();
    }

    @Override
    public boolean isAddedToMyOrder(ReportFrequency report) {
        boolean added = false;
        if (mySubscribedReports != null) {
            for (ReportFrequency r : mySubscribedReports) {
                if (r.reportId.equals(report.reportId)) {
                    added = true;
                    break;
                }
            }
        }
        return added;
    }

    @Override
    public boolean isInEditMode() {
        return editMode;
    }

    public void enterEditMode() {
        //adding null protection
        if (mySubscribedReports == null) {
            return;
        }
        drawMyOrders();
        //enter edit mode if not.
        if (!editMode) {
            updateEditMode();
        }
        //show the check image
        showCheckImage();
    }

    public void updateEditMode() {
        if (editMode) {
            editMode = false;
        } else {
            editMode = true;
        }
        showCheckImage();
    }

    private void showCheckImage() {
        int size = zReportFlowLayout.getChildCount();
        for (int i = 0; i < size; i++) {
            OrderReportFrameLayout f = (OrderReportFrameLayout) zReportFlowLayout.getChildAt(i);
            if (editMode) {
                f.checkImage.setVisibility(View.VISIBLE);
            } else {
                f.checkImage.setVisibility(View.GONE);
            }
        }
    }
}
