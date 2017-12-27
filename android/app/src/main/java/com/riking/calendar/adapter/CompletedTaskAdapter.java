package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
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

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    Realm realm;
    private List<Task> tasks;
    private int size;

    public CompletedTaskAdapter(Realm realm, List<Task> r) {
        this.realm = Realm.getDefaultInstance();
        ;
//        realm.addChangeListener(new RealmChangeListener<Realm>() {
//            @Override
//            public void onChange(Realm realm) {
//                notifyDataSetChanged();
//            }
//        });
        this.tasks = r;
        size = tasks.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_task_row, parent, false);
        return new MyViewHolder(tasks, itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Task r = tasks.get(position);
//        if(!r.isValid()){
//            notifyItemRemoved(position);
//            return;
//        }
        holder.position = position;
        holder.title.setText(r.title);
        if (r.isImportant == 1) {
        } else {
            holder.important.setImageDrawable(holder.important.getResources().getDrawable(R.drawable.not_important));
        }

        if (r.isComplete == 1) {
            holder.done.setImageDrawable(holder.done.getResources().getDrawable(R.drawable.work_icon_checkbox_s));
//            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.done.setImageDrawable(holder.done.getResources().getDrawable(R.drawable.not_done));
//            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.task = r;
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
        public Task task;

        public TextView tv;
        public int position;
        SwipeHorizontalMenuLayout sml;

        public MyViewHolder(final List<Task> tasks, View view) {
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
                    tasks.remove(task);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.where(Task.class).equalTo(Task.TODO_ID, task.todoId).findFirst().deleteFromRealm();
                        }
                    });

                    Toast.makeText(done.getContext(), "deleted", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
