package com.riking.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.QuestResult;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.viewholder.base.ZViewHolder;

import java.util.ArrayList;
import java.util.List;


public class MyQuestionsAdapter extends ZAdater<MyQuestionsAdapter.MyViewHolder> {
    private Context context;
    private List<QuestResult> mList;

    public MyQuestionsAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public void onBindVH(MyViewHolder holder, int position) {
        final QuestResult question = mList.get(position);
        holder.titleTV.setText(question.title);
        holder.contentTv.setVisibility(View.VISIBLE);
        holder.timeTv.setText(DateUtil.showTime(question.createdTime));
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_dynamic_answers_item, viewGroup, false);
        return new MyQuestionsAdapter.MyViewHolder(view);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    public void setData(List<QuestResult> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
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
