package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.OrderReportFrameLayout;
import com.riking.calendar.view.ZReportFlowLayout;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class OrderReportActivity extends AppCompatActivity {
    ZReportFlowLayout zReportFlowLayout;
    TextView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);
        init();
        /*//change the status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        StatusBarUtil.setTranslucent(this);*/
        for (int i = 0; i < 5; i++) {
            //inflate the item view from layout xml
            final OrderReportFrameLayout root = (OrderReportFrameLayout) LayoutInflater.from(this).inflate(R.layout.my_order_report_item, null);
            root.init();
            //set data
            root.reportNameTV.setText("G01000");
            zReportFlowLayout.addView(root);
        }
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initEvents() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = zReportFlowLayout.getChildCount();
                for (int i = 0; i < size; i++) {
                    OrderReportFrameLayout f = (OrderReportFrameLayout) zReportFlowLayout.getChildAt(i);
                    if (f.checkImage.getVisibility() == View.VISIBLE) {
                        f.checkImage.setVisibility(View.GONE);
                    } else {
                        f.checkImage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void initViews() {
        zReportFlowLayout = findViewById(R.id.flow_layout);
        button = findViewById(R.id.button);
    }
}
