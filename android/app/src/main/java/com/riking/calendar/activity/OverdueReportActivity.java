package com.riking.calendar.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.base.ZActivity;
import com.riking.calendar.adapter.CompletedReportHistoryAdapter;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.ReportCompletedRelParam;
import com.riking.calendar.pojo.server.ReportCompletedRelResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.viewholder.base.ZViewHolder;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/24.
 */

public class OverdueReportActivity extends ZActivity<OverdueReportActivity.CompleteReportHistoryAdapter> {

    @Override
    public CompleteReportHistoryAdapter getAdapter() {
        return new CompleteReportHistoryAdapter();
    }

    @Override
    public void loadData(final int page) {
        ReportCompletedRelParam param = new ReportCompletedRelParam();
        param.pindex = page;
        APIClient.findExpireTasks(param, new ZCallBackWithFail<ResponseModel<List<List<ReportCompletedRelResult>>>>() {
            @Override
            public void callBack(ResponseModel<List<List<ReportCompletedRelResult>>> response) {
                mPullToLoadView.setComplete();
                isLoading = false;
                if (failed) {

                } else {
                    List<List<ReportCompletedRelResult>> result = response._data;
                    if (result.size() == 0) {
                        ZToast.toast("没有更多数据了");
                        return;
                    }
                    MyLog.d("result size " + result.size());
                    mAdapter.setData(result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mPullToLoadView.getRecyclerView().invalidate();
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    nextPage = page + 1;
                }

            }
        });
    }

    @Override
    public void initViews() {
    }

    @Override
    public void initEvents() {
        activityTitle.setText("逾期任务");
    }

    public void clickBack(View view) {
        onBackPressed();
    }

    public class CompleteReportHistoryAdapter extends ZAdater<MyViewHolder, List<ReportCompletedRelResult>> {

    /*    @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.finished_task_row, parent, false);
            return new MyViewHolder(itemView);
        }*/
/*

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Log.d("zzw", "position: " + position + " key " + titles.get(position));
            holder.completedDate.setText("完成于" + titles.get(position));
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.completedDate.getContext()));
            holder.recyclerView.setAdapter(new CompletedReportHistoryAdapter(daysWithTasks.get(position)));
        }
*/

        @Override
        public void onBindVH(MyViewHolder holder, int position) {
            List<ReportCompletedRelResult> item = mList.get(position);
            String dateString = item.get(0).dateStr;
            Log.d("zzw", "position: " + position + " key " + dateString);
            holder.completedDate.setText("完成于" + dateString);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.completedDate.getContext()));
            holder.recyclerView.setAdapter(new CompletedReportHistoryAdapter(item));
        }

        @Override
        public MyViewHolder onCreateVH(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.finished_task_row, parent, false);
            return new MyViewHolder(itemView);
        }
    }

    public class MyViewHolder extends ZViewHolder {
        public TextView completedDate;
        public RecyclerView recyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            completedDate = (TextView) itemView.findViewById(R.id.completed_day);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.nested_recyclerview);
        }
    }
}
