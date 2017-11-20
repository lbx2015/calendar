package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.riking.calendar.R;
import com.riking.calendar.adapter.LocalSearchConditionAdapter;
import com.riking.calendar.realm.model.SearchConditions;
import com.riking.calendar.util.ZDB;

import io.realm.RealmResults;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SearchReportActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    View localSearchTitle;
    LocalSearchConditionAdapter localSearchConditionAdapter;

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
        recyclerView = findViewById(R.id.recycler_view);
        localSearchTitle = findViewById(R.id.local_search_title);
    }

    private void setRecyclerView() {
        //set layout manager for the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //adding dividers.
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        //set adapters
        RealmResults<SearchConditions> realmResults = ZDB.Instance.getRealm().where(SearchConditions.class).findAll();
        localSearchConditionAdapter = new LocalSearchConditionAdapter(this, realmResults);
        recyclerView.setAdapter(localSearchConditionAdapter);
    }

    public void localSearchConditionIsEmpty() {

    }
}
