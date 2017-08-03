package com.riking.calendar.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.util.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {
    private List<Reminder> reminders;

    public ReminderAdapter(List<Reminder> r) {
        this.reminders = r;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reminder_cardview_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Reminder r = reminders.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
        holder.time.setText(simpleDateFormat.format(r.time));
        holder.title.setText(r.title);

        //set a different color for the future reminders.
        if (DateUtil.get(r.day,r.time).compareTo(new Date()) > 0) {
            holder.time.setTextColor(ContextCompat.getColor(holder.time.getContext(), R.color.color_29a1f7));
            holder.title.setTextColor(ContextCompat.getColor(holder.title.getContext(), R.color.color_323232));
        }
    }

    @Override
    public int getItemCount() {
        Log.d("zzw", this + " getItemCount:" + reminders.size());
        return reminders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView title;

        public MyViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.time);
            title = (TextView) view.findViewById(R.id.title);
        }
    }
}
