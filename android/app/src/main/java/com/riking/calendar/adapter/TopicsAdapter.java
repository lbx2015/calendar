package com.riking.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.activity.AddTopicActivity;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.TopicResult;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.SearchTopicViewHolder;

import java.util.ArrayList;
import java.util.List;


public class TopicsAdapter extends ZAdater<SearchTopicViewHolder,TopicResult> {
    private Context context;

    public TopicsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SearchTopicViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.search_topic_item, viewGroup, false);
        return new SearchTopicViewHolder(view);
    }

    @Override
    public void onBindVH(final SearchTopicViewHolder h, int i) {
        TopicResult r = mList.get(i);
        h.title.setText(r.title);
        h.followButton.setVisibility(View.GONE);
        h.summary.setText(ZR.getNumberString(r.followNum) + "人关注");
        RequestOptions options = new RequestOptions();
        Glide.with(h.topicImage.getContext()).load(r.topicUrl)
                .apply(options.fitCenter().placeholder(R.drawable.banner))
                .into(h.topicImage);

        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof AddTopicActivity) {
                    AddTopicActivity a = (AddTopicActivity) context;
                    a.clickAddTopic(h.title.getText().toString());
                }
            }
        });
    }

}
