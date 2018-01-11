package com.riking.calendar.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.EditReminderActivity;
import com.riking.calendar.activity.WebviewActivity;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.fragment.WorkFragment;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.RCompletedRelParams;
import com.riking.calendar.pojo.server.CurrentReportTaskResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.base.ZViewHolder;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */
public class NotDoneReportTaskItemAdapter extends ZAdater<NotDoneReportTaskItemAdapter.MyViewHolder, CurrentReportTaskResp> {
    public CurrentReportTaskResp updateRemindReport;
    public int updateRemindPosition;
    WorkFragment fragment;

    public NotDoneReportTaskItemAdapter(WorkFragment fragment, List<CurrentReportTaskResp> r) {
        super(r);
        this.fragment = fragment;
    }

    @Override
    public void onBindVH(final MyViewHolder holder, final int position) {
        final CurrentReportTaskResp r = mList.get(position);
//        if(!r.isValid()){
//            notifyItemRemoved(position);
//            return;
//        }
        holder.position = position;
        holder.title.setText(r.reportCode);
        ZR.setReportName(holder.title, r.reportCode, r.frequency, r.reportBatch,r.reportId);
        holder.descriptTv.setText(r.reportName);

        //not enable the swipe function when user is not logged.
        if (ZPreference.pref.getBoolean(CONST.IS_LOGIN, false)) {
            holder.sml.setSwipeEnable(true);
        } else {
            holder.sml.setSwipeEnable(false);
        }

        holder.r = r;

        if (StringUtil.isEmpty(r.remindId)) {
            holder.clockImage.setVisibility(View.GONE);
        } else {
            holder.clockImage.setVisibility(View.VISIBLE);
        }

        //set complete complete click listener
        holder.buttonImage.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                RCompletedRelParams params = new RCompletedRelParams();
                //是否完成：0-未完成；1-完成
                params.isCompleted = 1;
                params.reportId = r.reportId;
                params.submitStartTime = r.submitStartTime;
                params.submitEndTime = r.submitEndTime;
                params.remindId = r.remindId;

                APIClient.completeReport(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        r.isCompleted = "1";
                        fragment.reportDoneTaskItemAdapter.appendStart(r);
                        NotDoneReportTaskItemAdapter.this.remmoveItem(r, position);
                        fragment.checkEmpty();
                    }
                });
            }
        });

        holder.tv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                updateRemindPosition = position;
                updateRemindReport = r;
                holder.sml.smoothCloseMenu();
                gotoEditRemindActivity(r);
            }
        });

        holder.clockImage.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                updateRemindPosition = position;
                updateRemindReport = r;
                holder.sml.smoothCloseMenu();
                gotoEditRemindActivity(r);
            }
        });
    }

    private void gotoEditRemindActivity(CurrentReportTaskResp r) {
        Intent i = new Intent(fragment.getContext(), EditReminderActivity.class);
        i.putExtra("reminder_id", r.remindId);
        i.putExtra("reminder_title", r.reportName);
        i.putExtra("submitStartTime", r.submitStartTime);
        i.putExtra("submitEndTime", r.submitEndTime);
        i.putExtra("reportId", r.reportId);
        fragment.startActivityForResult(i, CONST.REQUEST_CODE_ADD_REMINDER);
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends ZViewHolder {
        public TextView title;
        public View tv;
        public int position;
        public View divider;
        public TextView descriptTv;
        public ImageView buttonImage;
        public ImageView clockImage;
        SwipeHorizontalMenuLayout sml;
        CurrentReportTaskResp r;

        public MyViewHolder(View view) {
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
                    APIClient.apiInterface.getReportDetail(report).enqueue(new ZCallBack<ResponseModel<String>>() {
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
            tv = view.findViewById(R.id.tv_text);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
        }
    }
}