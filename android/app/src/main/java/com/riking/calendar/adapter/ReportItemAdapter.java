package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.helper.ItemTouchHelperAdapter;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    private List<QueryReport> reports;
    private int size;

    public ReportItemAdapter(List<QueryReport> r) {
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
        final QueryReport r = reports.get(position);
//        if(!r.isValid()){
//            notifyItemRemoved(position);
//            return;
//        }
        holder.position = position;
        holder.title.setText(r.reportName);

        holder.sml.setSwipeEnable(true);
        holder.r = r;
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
        SwipeHorizontalMenuLayout sml;
        QueryReport r;

        public MyViewHolder(final List<QueryReport> reports, View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.d("zzw", "report id: " + r.id);
                    apiInterface.getReportDetail(r).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ResponseBody body = response.body();
                            try {
                                Logger.d("zzw", body.source().readUtf8());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Logger.d("zzw", t.getMessage());
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
