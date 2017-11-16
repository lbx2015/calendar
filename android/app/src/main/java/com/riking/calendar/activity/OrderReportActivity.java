package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.InterestingReportLinearLayout;
import com.riking.calendar.view.ZReportFlowLayout;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class OrderReportActivity extends AppCompatActivity {
    ZReportFlowLayout zReportFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_report);
        zReportFlowLayout = findViewById(R.id.flow_layout);
        /*//change the status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        StatusBarUtil.setTranslucent(this);*/
        for (int i = 0; i < 5; i++) {
            //inflate the item view from layout xml
            final InterestingReportLinearLayout root = (InterestingReportLinearLayout) LayoutInflater.from(this).inflate(R.layout.intersting_report_item, null);
            //get view
            final TextView reportNameTV = root.findViewById(R.id.interesting_report);
            final ImageView checkImage = root.findViewById(R.id.check_image);
            //set data
            reportNameTV.setText("G01000");
            zReportFlowLayout.addView(root);
        }
    }
}
