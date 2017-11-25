package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.TopicActivity;
import com.riking.calendar.viewholder.QuestionsViewHolder;

import java.util.ArrayList;
import java.util.List;


public class QuestionsAdapter extends RecyclerView.Adapter {
    public List<String> mList;
    private Context context;

    public QuestionsAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.question_item, viewGroup, false);
        return new QuestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder cellHolder, int i) {
        QuestionsViewHolder h = (QuestionsViewHolder) cellHolder;
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, TopicActivity.class));
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(String s) {
        mList.add(s);
        notifyItemInserted(mList.size() - 1);
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
