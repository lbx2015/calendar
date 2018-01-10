package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.adapter.LocalSearchConditionAdapter;
import com.riking.calendar.adapter.ReportsOrderAdapter;
import com.riking.calendar.interfeet.PerformSearch;
import com.riking.calendar.interfeet.SubscribeReport;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.params.SubscribeReportParam;
import com.riking.calendar.pojo.server.ReportResult;
import com.riking.calendar.realm.model.SearchConditions;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZDB;
import com.riking.calendar.util.ZPreference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SearchReportActivity extends AppCompatActivity implements SubscribeReport<ReportResult>, PerformSearch {

    public ReportsOrderAdapter reportsOrderAdapter = new ReportsOrderAdapter(this);
    public boolean editMode = false;
    public String reportSearchCondition;
    RecyclerView recyclerView;
    View localSearchTitle;
    LocalSearchConditionAdapter localSearchConditionAdapter;
    EditText editText;
    List<ReportResult> orderReports;
    List<ReportResult> disOrderReports;
    ImageView clearSearchInputImage;
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
            ZPreference.put(CONST.ORDER_REPORTS_CHANGED, true);
            ZPreference.put(CONST.ORDER_REPORTS, gson.toJson(orderReports));
            ZPreference.put(CONST.DIS_ORDER_REPORTS, gson.toJson(disOrderReports));
        }
    }

    void init() {
        initViews();
        initEvents();
    }

    public void clickClearSearchConditions(View v) {
        ZDB.Instance.getRealm().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(SearchConditions.class).findAll().deleteAllFromRealm();
            }
        });
    }

    public void clickCancel(View view) {
        onBackPressed();
    }

    private void initEvents() {
        setRecyclerView();
        clearSearchInputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    private void initViews() {
        clearSearchInputImage = findViewById(R.id.cancel_search_button);
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
                    clearSearchInputImage.setVisibility(View.VISIBLE);
                } else {
                    clearSearchInputImage.setVisibility(View.GONE);
                }
            }
        });
    }

    public void performSearch() {
        SearchParams params = new SearchParams();
        params.keyWord = reportSearchCondition;
        //search reports
        params.objType = 1;
        APIClient.findSearchList(params, new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody r = response.body();
                //adding null protection
                if (r == null) {
                    return;
                }

                localSearchTitle.setVisibility(View.GONE);

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

                    List<ReportResult> list = responseModel._data;
                    reportsOrderAdapter.mList = list;
                    recyclerView.setAdapter(reportsOrderAdapter);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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

    public void localSearchConditionIsEmpty(boolean isEmpty) {

    }

    public void orderReport(ReportResult report) {
        saveToRealm();
        if (orderReports == null) {
            orderReports = new ArrayList<>();
        }

        subscribedReportsChanged = true;
        SubscribeReportParam a = new SubscribeReportParam();
        a.reportIds = report.reportId;
        a.subscribeType = 1;
        orderReports.add(report);
        APIClient.updateUserReportRelById(a, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {

            }
        });
    }

    public void unorderReport(ReportResult report) {
        saveToRealm();
        if (disOrderReports == null) {
            disOrderReports = new ArrayList<>();
        }

        subscribedReportsChanged = true;
        SubscribeReportParam a = new SubscribeReportParam();
        a.reportIds = report.reportId;
        a.subscribeType = 0;

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
    public boolean isAddedToMyOrder(ReportResult report) {
        return false;
    }

    @Override
    public boolean isInEditMode() {
        return editMode;
    }

    @Override
    public void performSearchByLocalHistory(String searchCondition) {
        reportSearchCondition = searchCondition;
        performSearch();
        saveToRealm();
    }
}
