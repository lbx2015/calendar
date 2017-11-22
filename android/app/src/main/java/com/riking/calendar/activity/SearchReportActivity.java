package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.riking.calendar.R;
import com.riking.calendar.adapter.LocalSearchConditionAdapter;
import com.riking.calendar.adapter.ReportsOrderAdapter;
import com.riking.calendar.interfeet.SubscribeReport;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUserReportRel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.ReportFrequency;
import com.riking.calendar.realm.model.SearchConditions;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;
import com.riking.calendar.util.ZDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SearchReportActivity extends AppCompatActivity implements SubscribeReport {

    public ReportsOrderAdapter reportsOrderAdapter = new ReportsOrderAdapter(this);
    public boolean editMode = false;
    RecyclerView recyclerView;
    View localSearchTitle;
    LocalSearchConditionAdapter localSearchConditionAdapter;
    EditText editText;
    List<ReportFrequency> orderReports;
    List<ReportFrequency> disOrderReports;
    public String reportSearchCondition;
    private boolean subscribedReportsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_report);
        init();

        editMode = getIntent().getExtras().getBoolean(CONST.EDIT_MODE, false);
        /*//change the status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        StatusBarUtil.setTranslucent(this);*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (subscribedReportsChanged) {
            Gson gson = new Gson();
            Preference.put(CONST.ORDER_REPORTS_CHANGED, true);
            Preference.put(CONST.ORDER_REPORTS, gson.toJson(orderReports));
            Preference.put(CONST.DIS_ORDER_REPORTS, gson.toJson(disOrderReports));
        }
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initEvents() {
        setRecyclerView();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        localSearchTitle = findViewById(R.id.local_search_title);
        editText = findViewById(R.id.search_edit_view);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    reportSearchCondition = s.toString();
                    performSearch();
                }
            }
        });
    }

    public void performSearch() {
        HashMap<String, String> reportName = new LinkedHashMap<>(1);
        reportName.put("reportName", reportSearchCondition);
        reportName.put("userId", Preference.pref.getString(CONST.USER_ID, ""));
        APIClient.getReportByName(reportName, new ZCallBack<ResponseModel<List<ReportFrequency>>>() {
            @Override
            public void callBack(ResponseModel<List<ReportFrequency>> response) {
                reportsOrderAdapter.mList = response._data;
                recyclerView.setAdapter(reportsOrderAdapter);
            }
        });
    }

    private void setRecyclerView() {
        //set layout manager for the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adding dividers.
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //set adapters
        RealmResults<SearchConditions> realmResults = ZDB.Instance.getRealm().where(SearchConditions.class).findAllSorted("updateTime", Sort.DESCENDING);
        localSearchConditionAdapter = new LocalSearchConditionAdapter(this, realmResults);
        recyclerView.setAdapter(localSearchConditionAdapter);
    }

    public void localSearchConditionIsEmpty() {

    }

    public void orderReport(ReportFrequency report) {
        saveToRealm();
        if (orderReports == null) {
            orderReports = new ArrayList<>();
        }

        subscribedReportsChanged = true;
        AppUserReportRel a = new AppUserReportRel();
        a.appUserId = Preference.pref.getString(CONST.USER_ID, "");
        a.reportId = report.reportId;
        a.reportName = report.reportName;
        a.type = "1";
        orderReports.add(report);
        APIClient.updateUserReportRelById(a, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
    }

    public void unorderReport(ReportFrequency report) {
        saveToRealm();
        if (disOrderReports == null) {
            disOrderReports = new ArrayList<>();
        }

        subscribedReportsChanged = true;
        AppUserReportRel a = new AppUserReportRel();
        a.appUserId = Preference.pref.getString(CONST.USER_ID, "");
        a.reportId = report.reportId;
        a.reportName = report.reportName;
        a.type = "0";

        disOrderReports.add(report);

        APIClient.updateUserReportRelById(a, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
    }

    public void saveToRealm() {
        ZDB.Instance.getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SearchConditions s = new SearchConditions();
                s.name = reportSearchCondition;
                s.updateTime = new Date();
                SearchConditions c = realm.copyToRealmOrUpdate(s);
            }
        });
    }

    @Override
    public boolean isAddedToMyOrder(ReportFrequency report) {
        return false;
    }

    @Override
    public boolean isInEditMode() {
        return editMode;
    }
}
