package com.riking.calendar.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.WebviewActivity;
import com.riking.calendar.helper.ItemTouchHelperAdapter;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.server.CurrentReportTaskResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */
public class ReportTaskItemAdapter extends RecyclerView.Adapter<ReportTaskItemAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    private List<CurrentReportTaskResp> reports;
    private int size;

    public ReportTaskItemAdapter(List<CurrentReportTaskResp> r) {
        this.reports = r;
        size = reports.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item_row, parent, false);
        return new MyViewHolder(reports, itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final CurrentReportTaskResp r = reports.get(position);
//        if(!r.isValid()){
//            notifyItemRemoved(position);
//            return;
//        }
        holder.position = position;
        holder.title.setText(r.reportCode);
        ZR.setReportName(holder.title, r.reportCode, r.frequency, r.reportBatch);
        holder.descriptTv.setText(r.reportName);

        //not enable the swipe function when user is not logged.
        if (ZPreference.pref.getBoolean(CONST.IS_LOGIN, false)) {
            holder.sml.setSwipeEnable(true);
        } else {
            holder.sml.setSwipeEnable(false);
        }

        holder.r = r;
        if (position == reports.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        } else {
            holder.divider.setVisibility(View.VISIBLE);
        }

        if (StringUtil.isEmpty(r.remindId)) {
            holder.clockImage.setVisibility(View.GONE);
        } else {
            holder.clockImage.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    @Override
    public void onItemDissmiss(int position) {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView tv;
        public int position;
        public View divider;
        public TextView descriptTv;
        public ImageView buttonImage;
        public ImageView clockImage;
        SwipeHorizontalMenuLayout sml;
        CurrentReportTaskResp r;

        public MyViewHolder(final List<CurrentReportTaskResp> reports, View view) {
            super(view);
            clockImage = view.findViewById(R.id.clock_image);
            buttonImage = view.findViewById(R.id.button_image);
            descriptTv = view.findViewById(R.id.descript_tv);
            title = (TextView) view.findViewById(R.id.title);
            divider = view.findViewById(R.id.divider);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Logger.d("zzw", "report userId: " + r.reportId);
                    QueryReport report = new QueryReport();
                    report.id = r.reportId;
                    apiInterface.getReportDetail(report).enqueue(new ZCallBack<ResponseModel<String>>() {
                        @Override
                        public void callBack(ResponseModel<String> response) {
                            String reportUrl = response._data;
                            Logger.d("zzw", "report Url : " + reportUrl);
                            if (reportUrl != null) {
                                Intent i = new Intent(title.getContext(), WebviewActivity.class);
                                i.putExtra(CONST.WEB_URL, reportUrl);
                                title.getContext().startActivity(i);
                            }
                        }
                    });
                }
            });
            tv = (TextView) view.findViewById(R.id.tv_text);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sml.smoothCloseMenu();
                    notifyItemRemoved(position);
                    //We should update the adapter after data set is changed. and we had not using RealmResult so for.
                    //so we need to update teh adapter manually
                    // reports.remove(task);
                    Toast.makeText(title.getContext(), "deleted", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
