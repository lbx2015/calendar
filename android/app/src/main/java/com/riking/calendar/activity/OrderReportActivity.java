package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.ReportFrequencyAdapter;
import com.riking.calendar.adapter.ReportsOrderAdapter;
import com.riking.calendar.interfeet.SubscribeReport;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.AppUserReportRel;
import com.riking.calendar.pojo.AppUserReportResult;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.ReportAgence;
import com.riking.calendar.pojo.server.ReportFrequency;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.OrderReportFrameLayout;
import com.riking.calendar.view.ZReportFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class OrderReportActivity extends AppCompatActivity implements SubscribeReport<ReportFrequency> {
    public ZReportFlowLayout zReportFlowLayout;
    public TextView editButton;
    public boolean editMode = false;
    public RecyclerView reportFrequencyRecyclerView;
    public RecyclerView reportsRecyclerViews;
    public ReportFrequencyAdapter reportFrequencyAdapter = new ReportFrequencyAdapter(this);
    public ReportsOrderAdapter reportsOrderAdapter = new ReportsOrderAdapter(this);
    public List<ReportAgence> reportAgences;
    //0 meansing CBOC , 1 means PBRC
    public int orgonizeType = 0;
    TextView firstGroupTv;
    TextView secondGroupTv;
    View firstGroupDivider;
    View secondGroupDivider;
    //user subscriber reports
    List<ReportFrequency> mySubscribedReports;
    FrameLayout myOrderLayout;
    View searchButton;

    public void clickBack(final View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);
        init();
        /*//change the status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        StatusBarUtil.setTranslucent(this);*/
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        firstGroupTv = findViewById(R.id.first_group);
        secondGroupTv = findViewById(R.id.second_group);
        firstGroupDivider = findViewById(R.id.first_group_divider);
        secondGroupDivider = findViewById(R.id.second_group_divider);

        zReportFlowLayout = findViewById(R.id.flow_layout);
        editButton = findViewById(R.id.button);
        reportFrequencyRecyclerView = findViewById(R.id.report_frequency_recycler_view);
        reportsRecyclerViews = findViewById(R.id.report_recycler_view);
        myOrderLayout = findViewById(R.id.my_order_title);
        searchButton = findViewById(R.id.search_button);
    }

    private void setClickListeners4Group() {
        firstGroupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orgonizeType == 1) {
                    //change the organization name
                    orgonizeType = 0;
                    secondGroupDivider.setVisibility(View.GONE);
                    secondGroupTv.setTextColor(ZR.getColor(R.color.color_222222));
                    firstGroupDivider.setVisibility(View.VISIBLE);
                    firstGroupTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                    //update the adapter
                    updateReportAgences();
                }
            }
        });

        secondGroupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orgonizeType == 0) {
                    //change the organization name
                    orgonizeType = 1;
                    firstGroupDivider.setVisibility(View.GONE);
                    firstGroupTv.setTextColor(ZR.getColor(R.color.color_222222));
                    secondGroupTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                    secondGroupDivider.setVisibility(View.VISIBLE);
                    //update the adapter
                    updateReportAgences();
                }
            }
        });
    }

    private void updateReportAgences() {
        if (reportAgences != null) {
            reportFrequencyAdapter.setData(reportAgences.get(orgonizeType).list);
        }
    }

    private void initEvents() {
        setRecyclerView();
        setClickListeners4Group();
        editButton.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                saveSubscribedReports();
                updateEditMode();
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OrderReportActivity.this, SearchReportActivity.class);
                i.putExtra(CONST.EDIT_MODE, editMode);
                ZGoto.to(i);
            }
        });
    }

    private void saveSubscribedReports() {
        if (editMode) {
            AppUserReportResult appUserReportResult = new AppUserReportResult();
            appUserReportResult.userId = ZPreference.pref.getString(CONST.USER_ID, "");
            appUserReportResult.list = new ArrayList<>();

            for (ReportFrequency r : mySubscribedReports) {
                appUserReportResult.list.add(r.reportId);
            }

            APIClient.userAddReportEdit(appUserReportResult, new ZCallBackWithFail<ResponseModel<Short>>() {
                @Override
                public void callBack(ResponseModel<Short> response) {
                    if (failed) {
                        Toast.makeText(OrderReportActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(OrderReportActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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

        reportsRecyclerViews.getRecycledViewPool().setMaxRecycledViews(0, 0);

        loadReport();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateOrderReports();
    }

    private void updateOrderReports() {
        if (ZPreference.pref.getBoolean(CONST.ORDER_REPORTS_CHANGED, false)) {
            ZPreference.put(CONST.ORDER_REPORTS_CHANGED, false);

            Gson gson = new Gson();
            final ReportFrequency[] orderReports = gson.fromJson(ZPreference.pref.getString(CONST.ORDER_REPORTS, ""), ReportFrequency[].class);
            final ReportFrequency[] disOrderReports = gson.fromJson(ZPreference.pref.getString(CONST.DIS_ORDER_REPORTS, ""), ReportFrequency[].class);
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

        //delete the preference for temp transfer value
        ZPreference.pref.edit().remove(CONST.ORDER_REPORTS_CHANGED).commit();
        ZPreference.pref.edit().remove(CONST.ORDER_REPORTS).commit();
        ZPreference.pref.edit().remove(CONST.DIS_ORDER_REPORTS).commit();
        reportsOrderAdapter.notifyDataSetChanged();
    }


    private void loadReport() {
        //request all reports
        AppUser u = new AppUser();
        //set user userId
        u.userId = ZPreference.pref.getString(CONST.USER_ID, "");
        APIClient.getAllReports(u, new ZCallBackWithFail<ResponseModel<List<ReportAgence>>>() {
            @Override
            public void callBack(ResponseModel<List<ReportAgence>> response) {
                if (failed) {

                } else {
                    reportAgences = response._data;
                    firstGroupTv.setText(reportAgences.get(0).agenceName);
                    secondGroupTv.setText(reportAgences.get(1).agenceName);
                    MyLog.d("reportAgences: " + reportAgences);
                    updateReportAgences();
                }
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
                final OrderReportFrameLayout root = (OrderReportFrameLayout) LayoutInflater.from(OrderReportActivity.this).inflate(R.layout.my_order_report_item, null);
                root.init();
                //set data
                root.reportNameTV.setText(appUserReportRel.reportName);
                root.checkImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zReportFlowLayout.removeView(root);
                        mySubscribedReports.remove(appUserReportRel);
                        int changedPosition = reportsOrderAdapter.mList.indexOf(appUserReportRel);

                        //keep the status of subscribe to be consistent in the page.
                        if (changedPosition > -1) {
                            reportsOrderAdapter.notifyItemChanged(changedPosition);
                        }
                        if (mySubscribedReports.size() == 0) {
                            //save the update when delete all the subscribed reports
                            saveSubscribedReports();
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
        a.appUserId = ZPreference.pref.getString(CONST.USER_ID, "");
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
            editButton.setText("编辑");
        } else {
            editButton.setText("保存");
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
