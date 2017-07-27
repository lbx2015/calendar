package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.realm.model.Vocation;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class VocationRecyclerViewAdapter extends RecyclerView.Adapter<VocationRecyclerViewAdapter.MyViewHolder> {
    private List<Vocation> vocationList;

    public VocationRecyclerViewAdapter(List<Vocation> r) {
        this.vocationList = r;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vocation_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Vocation r = vocationList.get(position);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
        holder.date.setText(simpleDateFormat.format(r.date));
        holder.country.setText(r.country);
        holder.currency.setText(r.currency);
        holder.vocation.setText(r.name);

    }

    @Override
    public int getItemCount() {
        Log.d("zzw", this + " getItemCount:" + vocationList.size());
        return vocationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, country, currency, vocation;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.date);
            country = (TextView) view.findViewById(R.id.country);
            currency = (TextView) view.findViewById(R.id.currency);
            vocation = (TextView) view.findViewById(R.id.vocation);
        }
    }
}
