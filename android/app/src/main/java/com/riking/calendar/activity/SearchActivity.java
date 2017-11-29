package com.riking.calendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.LocalSearchConditionAdapter;
import com.riking.calendar.interfeet.PerformSearch;
import com.riking.calendar.realm.model.SearchConditions;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;
import com.riking.calendar.util.ZDB;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SearchActivity extends AppCompatActivity implements PerformSearch {

    public String reportSearchCondition;
    View localSearchTitle;
    LocalSearchConditionAdapter localSearchConditionAdapter;
    EditText editText;
    RecyclerView recyclerView;
    ImageView clearSearchInputImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    void init() {
        initViews();
        initEvents();
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

    private void setRecyclerView() {
        //set layout manager for the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adding dividers.
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //set adapters
        RealmResults<SearchConditions> realmResults = ZDB.Instance.getRealm().where(SearchConditions.class).findAllSorted("updateTime", Sort.DESCENDING);
        localSearchConditionAdapter = new LocalSearchConditionAdapter(this, realmResults);
        //register realm change listener
        ZDB.Instance.getRealm().addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                localSearchConditionAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(localSearchConditionAdapter);
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        clearSearchInputImage = findViewById(R.id.cancel_search_button);
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            saveToRealm();
                        }
                    }, 1000);
                    clearSearchInputImage.setVisibility(View.VISIBLE);
                } else {
                    clearSearchInputImage.setVisibility(View.GONE);
                }
            }
        });
    }

    public void performSearch() {
        HashMap<String, String> reportName = new LinkedHashMap<>(1);
        reportName.put("reportName", reportSearchCondition);
        reportName.put("userId", Preference.pref.getString(CONST.USER_ID, ""));
      /*  APIClient.getReportByName(reportName, new ZCallBack<ResponseModel<List<ReportFrequency>>>() {
            @Override
            public void callBack(ResponseModel<List<ReportFrequency>> response) {
                reportsOrderAdapter.mList = response._data;
                recyclerView.setAdapter(reportsOrderAdapter);
            }
        });*/
    }

    @Override
    public void performSearchByLocalHistory(String searchCondition) {
        reportSearchCondition = searchCondition;
        performSearch();
        saveToRealm();
    }

    public void localSearchConditionIsEmpty() {

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

}
