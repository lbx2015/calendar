package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.pojo.server.ReportResult;
import com.riking.calendar.viewholder.HotAnswerOfTopicViewHolder;

import java.util.ArrayList;
import java.util.List;


public class HotAnswerOfTopicAdapter extends RecyclerView.Adapter<HotAnswerOfTopicViewHolder> {
    private Context a;
    private List<QAnswerResult> mList;

    public HotAnswerOfTopicAdapter(Context context) {
        this.a = context;
        mList = new ArrayList<>();
    }

    @Override
    public HotAnswerOfTopicViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.hot_answer_of_topic_item, viewGroup, false);
        return new HotAnswerOfTopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotAnswerOfTopicViewHolder h, int i) {
        if (i % 2 == 1) {
            h.answerTitle.setText("什么是证券投资基金？");
        } else {
            h.answerTitle.setText("证券业金融机构包括哪几类？");
        }
        RequestOptions options = new RequestOptions();
        Glide.with(h.authorImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.authorImage);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<QAnswerResult> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
