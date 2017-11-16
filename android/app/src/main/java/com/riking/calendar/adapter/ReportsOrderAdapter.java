package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.PositionSelectActivity;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.ReportFrequency;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.Preference;

import java.util.ArrayList;
import java.util.List;


public class ReportsOrderAdapter extends RecyclerView.Adapter<ReportOrderViewHolder> {

    public List<ReportFrequency> mList;
    private Context context;

    public ReportsOrderAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public ReportOrderViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.report_order_item, viewGroup, false);
        return new ReportOrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReportOrderViewHolder h, int i) {
        final ReportFrequency r = mList.get(i);
        h.reportTitle.setText(r.reportTitle);
        h.reportName.setText(r.reportName);
        h.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.checked) {
                    h.checked = false;
                    h.orderButton.setText("已订阅");
                    h.orderButton.setBackgroundColor(h.orderButton.getResources().getColor(R.color.white));
                } else {
                    h.checked = true;
                    h.orderButton.setText("订阅");
                    h.orderButton.setBackground(h.orderButton.getResources().getDrawable(R.drawable.rounded_order_button));
                }
            }
        });
        MyLog.d("reportName: " + h.reportName);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyApplication.mCurrentActivity, PositionSelectActivity.class);
//                i.putExtra(CONST.INDUSTRY_ID,r.reportId);
                //go to position select page
//                MyApplication.mCurrentActivity.startActivity(i);
                AppUser result = new AppUser();
//                result.industryId = industry.reportId;
                result.isGuide = "1";
                result.id = (Preference.pref.getString(CONST.USER_ID, ""));
                APIClient.updateUserInfo(result, new ZCallBackWithFail<ResponseModel<String>>() {
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

    public void setData(List<ReportFrequency> data) {
        this.mList = data;
        notifyDataSetChanged();
    }
}
