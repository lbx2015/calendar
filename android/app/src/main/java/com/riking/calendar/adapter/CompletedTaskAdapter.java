package com.riking.calendar.adapter;

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.helper.ItemTouchHelperAdapter;
import com.riking.calendar.realm.model.Task;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    private List<Task> tasks;

    public CompletedTaskAdapter(List<Task> r) {
        this.tasks = r;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Task r = tasks.get(position);
        holder.title.setText(r.title);
        if (r.isImport) {
            holder.important.setImageDrawable(holder.important.getResources().getDrawable(R.drawable.important));
        }

        if (r.isDone) {
            holder.done.setImageDrawable(holder.done.getResources().getDrawable(R.drawable.done));
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.task = r;

        holder.tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemRemoved(position);
                holder.sml.smoothCloseMenu();
              /*  fragment.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Task.class).equalTo("id", r.id).findFirst().deleteFromRealm();
                    }
                });*/
                Toast.makeText(holder.done.getContext(), "deleted", Toast.LENGTH_LONG).show();
            }
        });

        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("zzw", " on clicked iv **************");
            }
        });

        holder.sml.setSwipeEnable(true);
    }

    @Override
    public int getItemCount() {
        Log.d("zzw", this + " getItemCount:" + tasks.size());
        return tasks.size();
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
        public TextView iv;
        SwipeHorizontalMenuLayout sml;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            done = (ImageView) view.findViewById(R.id.done);
            important = (ImageView) view.findViewById(R.id.image_star);
            tv = (TextView) view.findViewById(R.id.tv_text);
            iv = (TextView) view.findViewById(R.id.tv_edit);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
        }
    }
}
