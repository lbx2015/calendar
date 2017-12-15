package com.riking.calendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.riking.calendar.R;
import com.riking.calendar.adapter.RelatedQuestionAdapter;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.server.QuestResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class RaiseQuestionActivity extends AppCompatActivity {
    TextInputEditText textInputEditText;
    TextView nextStep;
    RecyclerView recyclerView;
    RelatedQuestionAdapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    View divider;
    String searchCondition;

    public void clickCancel(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_question);
        init();
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initEvents() {
        setRecyclerView();
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZGoto.toWithLoginCheck(AddTopicActivity.class);
            }
        });
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    nextStep.setTextColor(ZR.getColor(R.color.color_489dfff));
                    nextStep.setEnabled(true);
                    search(s.toString());
                } else {
                    nextStep.setTextColor(ZR.getColor(R.color.color_cccccc));
                    nextStep.setEnabled(false);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                search(searchCondition);
            }
        });
    }

    public void search(String searchCondition) {
        if (TextUtils.isEmpty(searchCondition)) {
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        //keep the the search condition in order to refresh the page
        this.searchCondition = searchCondition;

        SearchParams params = new SearchParams();
        params.keyWord = searchCondition;
        //search questions
        params.objType = 5;

        APIClient.findSearchList(params, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                swipeRefreshLayout.setRefreshing(false);

                ResponseBody r = response.body();
                try {
                    String sourceString = r.source().readUtf8();
                    Gson s = new GsonBuilder().setDateFormat(CONST.YYYYMMDDHHMMSSSSS).create();
                    ;

                    JsonObject jsonObject = s.fromJson(sourceString, JsonObject.class);
                    String _data = jsonObject.get("_data").toString();

                    //do nothing when the data is empty.
                    if (TextUtils.isEmpty(_data.trim())) {
                        return;
                    }

                    TypeToken<ResponseModel<List<QuestResult>>> token = new TypeToken<ResponseModel<List<QuestResult>>>() {
                    };

                    ResponseModel<List<QuestResult>> responseModel = s.fromJson(sourceString, token.getType());

                    List<QuestResult> list = responseModel._data;

                    mAdapter.setData(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void initViews() {
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        recyclerView = findViewById(R.id.recycler_view);
        nextStep = findViewById(R.id.next_step);
        textInputEditText = findViewById(R.id.question_input);
    }

    private void setRecyclerView() {
        divider = findViewById(R.id.top_divider);
        //set layout manager for the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RelatedQuestionAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (mAdapter != null) {
                    if (mAdapter.getItemCount() == 0) {
                        divider.setVisibility(View.GONE);
                    } else {
                        divider.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

}
