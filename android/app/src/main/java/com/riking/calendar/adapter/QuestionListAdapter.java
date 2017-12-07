package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.pojo.server.QuestionAnswer;
import com.riking.calendar.viewholder.AnswerListViewHolder;

import java.util.ArrayList;
import java.util.List;


public class QuestionListAdapter extends RecyclerView.Adapter<AnswerListViewHolder> {
    public List<QuestionAnswer> mList;
    private Context a;

    public QuestionListAdapter(Context context) {
        this.a = context;
        mList = new ArrayList<>();
    }

    @Override
    public AnswerListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.question_list_item, viewGroup, false);
        return new AnswerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerListViewHolder h, int i) {
        RequestOptions options = new RequestOptions();
        Glide.with(h.authorImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.authorImage);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<QuestionAnswer> mList) {
        this.mList.clear();
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
