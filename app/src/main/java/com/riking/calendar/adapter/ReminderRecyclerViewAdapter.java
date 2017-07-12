package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.util.LunarCalendar;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class ReminderRecyclerViewAdapter extends RecyclerView.Adapter<ReminderRecyclerViewAdapter.MyViewHolder> {
    LunarCalendar lunarCalendar = new LunarCalendar();
    private List<Reminder> reminders;

    public ReminderRecyclerViewAdapter(List<Reminder> r) {
        this.reminders = r;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Reminder r = reminders.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd hh:mm:ss");

        holder.time.setText(simpleDateFormat.format(r.time));
        holder.title.setText(r.title);
    }

    @Override
    public int getItemCount() {
        Log.d("zzw", this + " getItemCount:" + reminders.size());
        return reminders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, time;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            time = (TextView) view.findViewById(R.id.time);
        }
    }
}
