package com.riking.calendar.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.CreateTaskActivity;
import com.riking.calendar.activity.EditTaskActivity;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.helper.ItemTouchHelperAdapter;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    //两个final int类型表示ViewType的两种类型
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1111;
    Realm realm;
    private RealmResults<Task> tasks;

    public TaskAdapter(RealmResults<Task> r, Realm realm) {
        this.tasks = r;
        this.realm = realm;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == NORMAL_TYPE) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.task_row, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.create_task_footer_row, parent, false);
        }

        return new MyViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (getItemViewType(position) == NORMAL_TYPE) {
            final Task r = tasks.get(position);
            holder.title.setText(r.title);
            if (r.isImportant == 1) {
                holder.important.setImageDrawable(holder.important.getResources().getDrawable(R.drawable.important));
            } else {
                holder.important.setImageDrawable(holder.important.getResources().getDrawable(R.drawable.not_important));
            }

            if (r.isComplete == 1) {
                holder.done.setImageDrawable(holder.done.getResources().getDrawable(R.drawable.done));
                holder.title.setTextColor(holder.title.getContext().getResources().getColor(R.color.color_background_b6b6b6));
                holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.done.setImageDrawable(holder.done.getResources().getDrawable(R.drawable.not_done));
                holder.title.setTextColor(holder.title.getContext().getResources().getColor(R.color.color_323232));
                holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            holder.task = r;

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.sml.smoothCloseMenu();
                    APIClient.synchronousTasks(r, CONST.DELETE);
                    Toast.makeText(holder.done.getContext(), "删除成功", Toast.LENGTH_LONG).show();
                }
            });

            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), EditTaskActivity.class);
                    i.putExtra("task_id", r.todo_Id);
                    i.putExtra("task_title", r.title);
                    i.putExtra("is_import", r.isImportant);
                    i.putExtra("is_remind", r.isOpen);
                    if (r.isOpen == 1) {
                        try {
                            i.putExtra("remind_time", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new SimpleDateFormat(CONST.yyyyMMddHHmm).parse(r.strDate)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    holder.sml.smoothCloseMenu();
                    v.getContext().startActivity(i);
                }
            });

            holder.sml.setSwipeEnable(true);
            //hide the last item's divider line
          /*  if (position + 1 == tasks.size()) {
                holder.divider.setVisibility(View.GONE);
            } else {
                holder.divider.setVisibility(View.VISIBLE);
            }*/
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //adding to do s
                    MyApplication.mCurrentActivity.startActivity(new Intent(v.getContext(), CreateTaskActivity.class));
//                    MyApplication.mCurrentActivity.startActivity(new Intent(v.getContext(), AddRemindActivity.class));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        //The last item is footer view
        return tasks.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == tasks.size()) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }

    @Override
    public void onItemDissmiss(int position) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView done;
        public ImageView important;
        public Task task;

        public ImageView deleteButton;
        public ImageView editButton;
        SwipeHorizontalMenuLayout sml;
        //        View divider;
        boolean completed = false;

        public MyViewHolder(View view, int type) {
            super(view);
            if (type == NORMAL_TYPE) {
                title = (TextView) view.findViewById(R.id.title);
                done = (ImageView) view.findViewById(R.id.done);
                important = (ImageView) view.findViewById(R.id.image_star);
                deleteButton =  view.findViewById(R.id.tv_text);
                editButton =  view.findViewById(R.id.tv_edit);
                sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
//                divider = view.findViewById(R.userId.divider);
                final Handler handler = new Handler();
                done.setOnClickListener(new View.OnClickListener() {
                    Runnable callBack = new Runnable() {
                        @Override
                        public void run() {
                            realm.executeTransaction
                                    (new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            Task t;
                                            if (completed) {
                                                t = realm.where(Task.class).equalTo(Task.TODO_ID, task.todo_Id).findFirst();
                                                t.isComplete = 1;
                                                t.completeDate = new SimpleDateFormat(CONST.yyyyMMddHHmm).format(new Date());
                                            }
                                        }
                                    });
                        }
                    };

                    @Override
                    public void onClick(View v) {
                        if (completed) {
                            completed = false;
                            done.setImageDrawable(done.getResources().getDrawable(R.drawable.not_done));
                            title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                            //cancel the pending runnable to complete the task
                            handler.removeCallbacks(callBack);
                        } else {
                            completed = true;
                            done.setImageDrawable(done.getResources().getDrawable(R.drawable.done));
                            title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            //complete the task after 5 seconds delay
                            handler.postDelayed(callBack, 5000);
                        }
                    }
                });

                important.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        realm.executeTransaction
                                (new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        if (task.isImportant == 1) {
                                            important.setImageDrawable(important.getResources().getDrawable(R.drawable.not_important));
                                            realm.where(Task.class).equalTo(Task.TODO_ID, task.todo_Id).findFirst().isImportant = 0;
                                        } else {
                                            important.setImageDrawable(important.getResources().getDrawable(R.drawable.important));
                                            realm.where(Task.class).equalTo(Task.TODO_ID, task.todo_Id).findFirst().isImportant = 1;
                                        }
                                    }
                                });
                    }
                });
            }
        }
    }
}
