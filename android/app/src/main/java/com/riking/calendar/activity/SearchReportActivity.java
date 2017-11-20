package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.ReportFrequencyAdapter;
import com.riking.calendar.adapter.ReportsOrderAdapter;
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
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.OrderReportFrameLayout;
import com.riking.calendar.view.ZReportFlowLayout;
import com.riking.calendar.widget.dialog.SearchDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SearchReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_report);
        init();
        /*//change the status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        StatusBarUtil.setTranslucent(this);*/
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initEvents() {

    }

    private void initViews() {
    }

}
