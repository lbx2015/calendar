package com.riking.calendar.adapter;

import android.content.Intent;
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
import com.riking.calendar.activity.EditTaskActivity;
import com.riking.calendar.fragment.TaskFragment;
import com.riking.calendar.helper.ItemTouchHelperAdapter;
import com.riking.calendar.realm.model.Task;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    TaskFragment fragment;
    private RealmResults<Task> tasks;

    public TaskAdapter(RealmResults<Task> r, TaskFragment fragment) {
        this.tasks = r;
        this.fragment = fragment;
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
        Log.d("zzw","need to remind " + r.isReminded);
        if (r.isReminded ==1) {
            holder.remindTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(r.remindTime));
        } else {
            holder.remindTime.setText(null);
        }
        if (r.isImport==1) {
            holder.important.setImageDrawable(holder.important.getResources().getDrawable(R.drawable.important));
        } else {
            holder.important.setImageDrawable(holder.important.getResources().getDrawable(R.drawable.not_important));
        }

        if (r.isDone==1) {
            holder.done.setImageDrawable(holder.done.getResources().getDrawable(R.drawable.done));
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.done.setImageDrawable(holder.done.getResources().getDrawable(R.drawable.not_done));
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.task = r;

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemRemoved(position);
                holder.sml.smoothCloseMenu();
                fragment.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Task.class).equalTo("id", r.id).findFirst().deleteFromRealm();
                    }
                });
                Toast.makeText(holder.done.getContext(), "deleted", Toast.LENGTH_LONG).show();
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditTaskActivity.class);
                i.putExtra("task_id", r.id);
                i.putExtra("task_title", r.title);
                i.putExtra("is_import", r.isImport);
                i.putExtra("is_remind", r.isReminded);
                if (r.isReminded==1) {
                    i.putExtra("remind_time", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(r.remindTime));
                }
                holder.sml.smoothCloseMenu();
                v.getContext().startActivity(i);
            }
        });

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
        public TextView remindTime;

        public TextView deleteButton;
        public TextView editButton;
        SwipeHorizontalMenuLayout sml;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            done = (ImageView) view.findViewById(R.id.done);
            important = (ImageView) view.findViewById(R.id.image_star);
            deleteButton = (TextView) view.findViewById(R.id.tv_text);
            editButton = (TextView) view.findViewById(R.id.tv_edit);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
            remindTime = (TextView) view.findViewById(R.id.remind_time);

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    fragment.realm.executeTransaction
                            (new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    Task t;
                                    if (task.isDone==1) {
                                        done.setImageDrawable(done.getResources().getDrawable(R.drawable.not_done));
                                        title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                                        t = realm.where(Task.class).equalTo("id", task.id).findFirst();
                                        t.isDone = 0;
                                        t.completeDay = null;
                                    } else {
                                        done.setImageDrawable(done.getResources().getDrawable(R.drawable.done));
                                        title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                        t = realm.where(Task.class).equalTo("id", task.id).findFirst();
                                        t.isDone = 1;
                                        t.completeDay = new Date();
                                    }
                                }
                            });
                }
            });

            important.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    fragment.realm.executeTransaction
                            (new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    if (task.isImport==1) {
                                        important.setImageDrawable(important.getResources().getDrawable(R.drawable.not_important));
                                        realm.where(Task.class).equalTo("id", task.id).findFirst().isImport = 0;
                                    } else {
                                        important.setImageDrawable(important.getResources().getDrawable(R.drawable.important));
                                        realm.where(Task.class).equalTo("id", task.id).findFirst().isImport = 1;
                                    }
                                }
                            });
                }
            });
        }


    }
}
