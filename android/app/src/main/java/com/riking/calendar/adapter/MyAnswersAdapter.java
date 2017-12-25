package com.riking.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.viewholder.base.ZViewHolder;

import java.util.ArrayList;
import java.util.List;


public class MyAnswersAdapter extends ZAdater<MyAnswersAdapter.MyViewHolder,QAnswerResult> {
    private Context context;

    public MyAnswersAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindVH(MyViewHolder holder, int position) {
        final QAnswerResult answer = mList.get(position);
        holder.titleTV.setText(answer.title);
        holder.contentTv.setText(answer.content);
        holder.timeTv.setText(DateUtil.showTime(answer.createdTime));
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_dynamic_answers_item, viewGroup, false);
        return new MyAnswersAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends ZViewHolder {
        public TextView titleTV;
        public TextView contentTv;
        public TextView timeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.title);
            contentTv = itemView.findViewById(R.id.content);
            timeTv = itemView.findViewById(R.id.update_time);
        }
    }
}
