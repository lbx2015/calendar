package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.TopicsAdapter;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.OrderReportFrameLayout;
import com.riking.calendar.view.ZReportFlowLayout;

import java.util.ArrayList;
import java.util.List;

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
    }


    private void initEvents() {
        setRecyclerView();
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
                //todo test only
                mAdapter.add("a");
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
                final OrderReportFrameLayout root = (OrderReportFrameLayout) LayoutInflater.from(AddTopicActivity.this).inflate(R.layout.my_order_report_item, null);
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
