package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

public class WriteAnswerActivity extends AppCompatActivity {


    public void clickCancel(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);
        init();
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
