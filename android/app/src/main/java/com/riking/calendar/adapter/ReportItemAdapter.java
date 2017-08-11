package com.riking.calendar.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.helper.ItemTouchHelperAdapter;
import com.riking.calendar.pojo.Report;
import com.riking.calendar.realm.model.Task;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class ReportItemAdapter extends RecyclerView.Adapter<ReportItemAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    private List<Report> reports;
    private int size;

    public ReportItemAdapter(List<Report> r) {
        this.reports = r;
        size = reports.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_task_row, parent, false);
        return new MyViewHolder(reports, itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Report r = reports.get(position);
//        if(!r.isValid()){
//            notifyItemRemoved(position);
//            return;
//        }
        holder.position = position;
        holder.title.setText(r.reportName);

        holder.sml.setSwipeEnable(true);
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
        public ImageView done;
        public ImageView important;
        public Task task;

        public TextView tv;
        public int position;
        SwipeHorizontalMenuLayout sml;

        public MyViewHolder(final List<Report> reports, View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            done = (ImageView) view.findViewById(R.id.done);
            important = (ImageView) view.findViewById(R.id.image_star);
            tv = (TextView) view.findViewById(R.id.tv_text);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sml.smoothCloseMenu();
                    notifyItemRemoved(position);
                    //We should update the adapter after data set is changed. and we had not using RealmResult so for.
                    //so we need to update teh adapter manually
                    reports.remove(task);
                    Toast.makeText(done.getContext(), "deleted", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
