package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.helper.ItemTouchHelperAdapter;
import com.riking.calendar.pojo.server.ReportCompletedRelResult;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */
public class CompletedReportHistoryAdapter extends RecyclerView.Adapter<CompletedReportHistoryAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    private List<ReportCompletedRelResult> tasks;
    private int size;

    public CompletedReportHistoryAdapter(List<ReportCompletedRelResult> r) {
        this.tasks = r;
        size = tasks.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_report_row, parent, false);
        return new MyViewHolder(tasks, itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ReportCompletedRelResult r = tasks.get(position);

        holder.position = position;
        holder.title.setText(r.reportName);

        holder.report = r;
        holder.sml.setSwipeEnable(true);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public void onItemDissmiss(int position) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView done;
        public ImageView important;
        public ReportCompletedRelResult report;

        public TextView tv;
        public int position;
        SwipeHorizontalMenuLayout sml;

        public MyViewHolder(final List<ReportCompletedRelResult> tasks, View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            done = (ImageView) view.findViewById(R.id.done);
            important = (ImageView) view.findViewById(R.id.image_star);
            tv = (TextView) view.findViewById(R.id.tv_text);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
            sml.setSwipeEnable(false);
           /* tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sml.smoothCloseMenu();
                    Todo todo = new Todo(task);
                    todo.deleteFlag = 1;
                    final ArrayList<Todo> todos = new ArrayList<>(1);
                    todos.add(todo);
                    APIClient.saveTodo(todos, new ZCallBackWithFail<ResponseModel<String>>() {
                        @Override
                        public void callBack(ResponseModel<String> response) throws Exception {
                            if (failed) {

                            } else {
                                //We should update the adapter after data set is changed. and we had not using RealmResult so for.
                                //so we need to update teh adapter manually
                                tasks.remove(task);
                                notifyDataSetChanged();
                                Toast.makeText(done.getContext(), "deleted", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });*/
        }
    }
}
