package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.riking.calendar.R;
import com.riking.calendar.adapter.InvitePersonAdapter;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.pojo.server.ReportFrequency;
import com.riking.calendar.pojo.server.ReportResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZPreference;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class InvitePersonActivity extends AppCompatActivity {

    public String searchCondition;
    InvitePersonAdapter mAdapter;
    RecyclerView recyclerView;
    View localSearchTitle;
    EditText editText;
    List<ReportResult> orderReports;
    List<ReportResult> disOrderReports;
    ImageView clearSearchInputImage;
    private boolean subscribedReportsChanged = false;
    public String questionId;

    public void clickCancel(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_person);
        init();
        questionId = getIntent().getStringExtra(CONST.QUESTION_ID);
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
                    searchCondition = s.toString();
                    performSearch();
                    clearSearchInputImage.setVisibility(View.VISIBLE);
                } else {
                    clearSearchInputImage.setVisibility(View.GONE);
                }
            }
        });
    }

    public void performSearch() {

        if (TextUtils.isEmpty(searchCondition)) {
            return;
        }

        this.searchCondition = searchCondition;

        SearchParams params = new SearchParams();
        //invite status
        params.showOptType = 2;
        params.keyWord = searchCondition;
        //search person
        params.objType = 3;

        APIClient.findSearchList(params, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ResponseBody r = response.body();
                try {
                    String sourceString = r.source().readUtf8();
                    Gson s = new Gson();

                    JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                    String _data = jsonObject.get("_data").toString();

                    //do nothing when the data is empty.
                    if (TextUtils.isEmpty(_data.trim())) {
                        return;
                    }

                    TypeToken<ResponseModel<List<AppUserResult>>> token = new TypeToken<ResponseModel<List<AppUserResult>>>() {
                    };

                    ResponseModel<List<AppUserResult>> responseModel = s.fromJson(sourceString, token.getType());

                    List<AppUserResult> list = responseModel._data;

                    if (list.isEmpty()) {
                        Toast.makeText(InvitePersonActivity.this, "没有更多数据了",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mAdapter.setData(list);
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
        mAdapter = new InvitePersonAdapter(this);
        recyclerView.setAdapter(mAdapter);
    }

}
