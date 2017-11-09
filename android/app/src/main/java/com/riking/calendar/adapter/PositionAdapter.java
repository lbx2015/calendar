package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.ReportsSelectActivity;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.pojo.server.Industry;

import java.util.ArrayList;
import java.util.List;


public class PositionAdapter extends RecyclerView.Adapter<PositionViewHolder> {

    public List<Industry> mList;
    private Context context;

    public PositionAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public PositionViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.industry_item, viewGroup, false);
        return new PositionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PositionViewHolder h, int i) {
        h.positionName.setText(mList.get(i).name);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.checkImage.getVisibility() == View.VISIBLE) {
                    h.checkImage.setVisibility(View.GONE);
                    h.positionName.setTextColor(h.positionName.getResources().getColor(R.color.black_deep));
                } else {
                    h.checkImage.setVisibility(View.VISIBLE);
                    h.positionName.setTextColor(h.positionName.getResources().getColor(R.color.holidayColor));
                }
                MyApplication.mCurrentActivity.startActivity(new Intent(MyApplication.mCurrentActivity, ReportsSelectActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
