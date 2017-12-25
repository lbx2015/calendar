package com.riking.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.QuestResult;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.base.ZViewHolder;


public class MyFollowingQuestionsAdapter extends ZAdater<MyFollowingQuestionsAdapter.MyViewHolder, QuestResult> {
    private Context context;

    public MyFollowingQuestionsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindVH(MyViewHolder holder, int position) {
        final QuestResult question = mList.get(position);
        holder.titleTV.setText(question.title);
        holder.contentTv.setVisibility(View.GONE);
        holder.timeTv.setText(ZR.getNumberString(question.tqFollowNum) + "个关注" + ZR.getNumberString(question.qanswerNum) + "个回答");
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_dynamic_answers_item, viewGroup, false);
        return new MyFollowingQuestionsAdapter.MyViewHolder(view);
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
