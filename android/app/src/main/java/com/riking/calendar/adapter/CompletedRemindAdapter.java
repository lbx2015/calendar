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
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class CompletedRemindAdapter extends RecyclerView.Adapter<CompletedRemindAdapter.MyViewHolder> implements ItemTouchHelperAdapter {
    Realm realm;
    private List<Reminder> reminders;
    private int size;

    public CompletedRemindAdapter(Realm realm, List<Reminder> r) {
        this.realm = Realm.getDefaultInstance();
        ;
//        realm.addChangeListener(new RealmChangeListener<Realm>() {
//            @Override
//            public void onChange(Realm realm) {
//                notifyDataSetChanged();
//            }
//        });
        this.reminders = r;
        size = reminders.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_reminder_row, parent, false);
        return new MyViewHolder(reminders, itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Reminder r = reminders.get(position);
//        if(!r.isValid()){
//            notifyItemRemoved(position);
//            return;
//        }
        holder.position = position;
        holder.title.setText(r.title);

        holder.reminder = r;
        holder.sml.setSwipeEnable(true);
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    @Override
    public void onItemDissmiss(int position) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView done;
        public ImageView important;
        public Reminder reminder;

        public TextView tv;
        public int position;
        SwipeHorizontalMenuLayout sml;

        public MyViewHolder(final List<Reminder> tasks, View view) {
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
                    tasks.remove(reminder);
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.where(Task.class).equalTo(Task.TODO_ID, reminder.id).findFirst().deleteFromRealm();
                        }
                    });

                    Toast.makeText(done.getContext(), "deleted", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
