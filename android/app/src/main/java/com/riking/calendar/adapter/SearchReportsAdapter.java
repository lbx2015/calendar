package com.riking.calendar.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.PositionSelectActivity;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.interfeet.SubscribeReport;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UpdUserParams;
import com.riking.calendar.pojo.server.ReportResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;

import java.util.ArrayList;
import java.util.List;


public class SearchReportsAdapter extends RecyclerView.Adapter<ReportOrderViewHolder> {

    public List<ReportResult> mList;
    private SubscribeReport subscribeReportListener;

    public SearchReportsAdapter(SubscribeReport subscribeReportListener) {
        this.subscribeReportListener = subscribeReportListener;
        mList = new ArrayList<>();
    }

    @Override
    public ReportOrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.report_item, viewGroup, false);
        return new ReportOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReportOrderViewHolder h, int i) {
        final ReportResult r = mList.get(i);
        h.reportTitle.setText(r.title);
        ZR.setReportName(h.code, r.code, r.frequency, r.reportBatch,r.reportId);
        if (r.isSubscribe == 0) {
            h.subscribed = false;
        } else {
            h.subscribed = true;
        }

        //show subscribe or not subscribed.
        if (h.subscribed) {
            h.orderButton.setText("已订阅");
            h.orderButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            h.orderButton.setTextColor(ZR.getColor(R.color.color_999999));
            h.orderButton.setBackground(h.orderButton.getResources().getDrawable(R.drawable.rounded_gray));
        } else {
            h.orderButton.setText("订阅");
            h.orderButton.setTextColor(ZR.getColor(R.color.color_489dfff));
            h.orderButton.setBackground(h.orderButton.getResources().getDrawable(R.drawable.rounded_order_button));
        }
        h.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.subscribed) {
                    h.subscribed = false;
                    h.orderButton.setText("订阅");
                    h.orderButton.setTextColor(ZR.getColor(R.color.color_489dfff));
                    h.orderButton.setBackground(h.orderButton.getResources().getDrawable(R.drawable.rounded_order_button));
                } else {
                    h.subscribed = true;
                    h.orderButton.setText("已订阅");
                    h.orderButton.setTextColor(ZR.getColor(R.color.color_999999));
                    h.orderButton.setBackground(h.orderButton.getResources().getDrawable(R.drawable.rounded_gray));
                }
                //update my orders
                if (h.subscribed) {
                    subscribeReportListener.orderReport(r);
                } else {
                    subscribeReportListener.unorderReport(r);
                }
            }
        });
        MyLog.d("reportName: " + h.code);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyApplication.mCurrentActivity, PositionSelectActivity.class);
//                i.putExtra(CONST.INDUSTRY_ID,r.reportId);
                //go to position select page
//                MyApplication.mCurrentActivity.startActivity(i);
                UpdUserParams result = new UpdUserParams();
//                result.industryId = industry.reportId;
                result.isGuide = 1;
                result.userId = (ZPreference.pref.getString(CONST.USER_ID, ""));
                APIClient.modifyUserInfo(result, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {

                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<ReportResult> data) {
        this.mList = data;
        notifyDataSetChanged();
    }
}
