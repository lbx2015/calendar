package com.riking.calendar.adapter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.EditReminderActivity;
import com.riking.calendar.fragment.ReminderFragment;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.widget.dialog.LookReminderDialog;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {
    ReminderFragment fragment;

    private List<Reminder> reminders;

    public ReminderAdapter(List<Reminder> r, ReminderFragment fragment) {
        this.reminders = r;
        this.fragment = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_cardview_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Reminder r = reminders.get(position);
        String mm = r.time.substring(2);
        String HH = r.time.substring(0, 2);
        holder.time.setText(HH + ":" + mm);
        holder.title.setText(r.title);
        Date today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        //set a different color for the future reminders.
        if (DateUtil.get(r.day, r.time).compareTo(today) > 0 && r.day.equals(dateFormat.format(today))) {
            holder.time.setTextColor(ContextCompat.getColor(holder.time.getContext(), R.color.color_29a1f7));
            holder.title.setTextColor(ContextCompat.getColor(holder.title.getContext(), R.color.color_323232));
        } else {
            holder.time.setTextColor(ContextCompat.getColor(holder.time.getContext(), R.color.color_888888));
            holder.title.setTextColor(ContextCompat.getColor(holder.title.getContext(), R.color.color_888888));
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemRemoved(position);
                holder.sml.smoothCloseMenu();
                fragment.realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.where(Reminder.class).equalTo("id", r.id).findFirst().deleteFromRealm();
                    }
                });
                Toast.makeText(holder.time.getContext(), "deleted", Toast.LENGTH_LONG).show();
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), EditReminderActivity.class);
                i.putExtra("reminder_id", r.id);
                i.putExtra("reminder_title", r.title);
                i.putExtra("remind_time", r.time);
                i.putExtra("remind_day", r.day);
                i.putExtra("is_all_day", r.isAllDay);
                i.putExtra("is_remind", r.isRemind);
                i.putExtra("ahead_time", r.aheadTime);
                i.putExtra("repeat_flag", r.repeatFlag);
                i.putExtra("repeat_week", r.repeatWeek);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                i.putExtra("repeat_date", sdf.format(r.reminderTime));
                holder.sml.smoothCloseMenu();
                v.getContext().startActivity(i);
            }
        });

        holder.sml.setSwipeEnable(true);
        holder.contentRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LookReminderDialog d = new LookReminderDialog(v.getContext(), r, fragment);
                d.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView title;

        public TextView deleteButton;
        public TextView editButton;
        SwipeHorizontalMenuLayout sml;
        View contentRow;

        public MyViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.time);
            title = (TextView) view.findViewById(R.id.title);
            deleteButton = (TextView) view.findViewById(R.id.tv_text);
            editButton = (TextView) view.findViewById(R.id.tv_edit);
            sml = (SwipeHorizontalMenuLayout) itemView.findViewById(R.id.sml);
            contentRow = view.findViewById(R.id.smContentView);
        }
    }
}
