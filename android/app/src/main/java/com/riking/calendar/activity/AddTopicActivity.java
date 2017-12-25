package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.riking.calendar.R;
import com.riking.calendar.adapter.TopicsAdapter;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.SearchParams;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.pojo.server.TopicResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.OrderReportFrameLayout;
import com.riking.calendar.view.ZReportFlowLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class AddTopicActivity extends AppCompatActivity {
    public ZReportFlowLayout zReportFlowLayout;
    public boolean editMode = false;
    public RecyclerView topicRecyclerView;
    public TopicsAdapter mAdapter;
    //user subscriber reports
    List<String> mySubscribedReports = new ArrayList<>();
    TextInputEditText textInputEditText;
    TextView nextStep;
    String searchCondition;
    SwipeRefreshLayout swipeRefreshLayout;

    public void clickBack(final View view) {
        onBackPressed();
    }

    public void clickAddTopic(String s) {
        if (mySubscribedReports.size() == 3 || isAddedToMyOrder(s)) {
            return;
        }
        mySubscribedReports.add(s);
        enterEditMode();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_topic);
        init();
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initViews() {
        nextStep = findViewById(R.id.next_step);
        textInputEditText = findViewById(R.id.search_topic);
        zReportFlowLayout = findViewById(R.id.flow_layout);
        topicRecyclerView = findViewById(R.id.recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void initEvents() {
        setRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                search(searchCondition);
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
                } else {
                    nextStep.setTextColor(ZR.getColor(R.color.color_cccccc));
                    nextStep.setEnabled(false);
                }
                search(s.toString());
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
        //search topics
        params.objType = 2;

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

                    TypeToken<ResponseModel<List<Topic>>> token = new TypeToken<ResponseModel<List<Topic>>>() {
                    };

                    ResponseModel<List<Topic>> responseModel = s.fromJson(sourceString, token.getType());

                    List<Topic> list = responseModel._data;

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

    private void setRecyclerView() {
        //set layout manager for the recycler view.
        topicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TopicsAdapter(this);
        //set adapters
        topicRecyclerView.setAdapter(mAdapter);
    }

    public void drawMyOrders() {
        //redraw the reports
        zReportFlowLayout.removeAllViews();
        if (mySubscribedReports.size() > 0) {
            zReportFlowLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mySubscribedReports.size(); i++) {
                final String appUserReportRel = mySubscribedReports.get(i);
                //inflate the item view from layout xml
                final OrderReportFrameLayout root = (OrderReportFrameLayout) LayoutInflater.from(AddTopicActivity.this).inflate(R.layout.add_topic_for_question_item, null);
                root.init();
                //set data
                root.reportNameTV.setText(appUserReportRel);
                root.checkImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zReportFlowLayout.removeView(root);
                        mySubscribedReports.remove(appUserReportRel);
                        if (mySubscribedReports.size() == 0) {
                            //hide the title
                            zReportFlowLayout.setVisibility(View.GONE);
                        }
                    }
                });
                zReportFlowLayout.addView(root);
            }
        } else {
            //hide the title of the orders
            zReportFlowLayout.setVisibility(View.GONE);
        }
    }

    public boolean isAddedToMyOrder(String report) {
        boolean added = false;
        if (mySubscribedReports != null) {
            for (String r : mySubscribedReports) {
                if (r.equals(report)) {
                    added = true;
                    break;
                }
            }
        }
        return added;
    }

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
        } else {
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
