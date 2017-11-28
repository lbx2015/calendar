package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.riking.calendar.R;
import com.riking.calendar.adapter.InvitePersonAdapter;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.ReportFrequency;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class InvitePersonActivity extends AppCompatActivity {

    public String reportSearchCondition;
    InvitePersonAdapter mAdapter;
    RecyclerView recyclerView;
    View localSearchTitle;
    EditText editText;
    List<ReportFrequency> orderReports;
    List<ReportFrequency> disOrderReports;
    private boolean subscribedReportsChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_person);
        init();

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
        //todo test only code (remove the following line later)
        mAdapter.add("dd");
        HashMap<String, String> reportName = new LinkedHashMap<>(1);
        reportName.put("reportName", reportSearchCondition);
        reportName.put("userId", Preference.pref.getString(CONST.USER_ID, ""));
        APIClient.getReportByName(reportName, new ZCallBack<ResponseModel<List<ReportFrequency>>>() {
            @Override
            public void callBack(ResponseModel<List<ReportFrequency>> response) {

            }
        });
    }

    private void setRecyclerView() {
        //set layout manager for the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new InvitePersonAdapter(this);
        mAdapter.add("dd");
        mAdapter.add("ddlllljjijjlkkj");
        recyclerView.setAdapter(mAdapter);
    }

}
