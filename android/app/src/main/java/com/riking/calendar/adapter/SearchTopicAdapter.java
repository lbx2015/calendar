package com.riking.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.SearchTopicViewHolder;


public class SearchTopicAdapter extends ZAdater<SearchTopicViewHolder, Topic> {

    @Override
    public void onBindVH(final SearchTopicViewHolder h, int i) {
        Topic topicResult = mList.get(i);
        h.title.setText(topicResult.title);
        h.summary.setText(ZR.getNumberString(topicResult.followNum) + "人关注");

        ZR.setImage(h.topicImage, topicResult.topicUrl);

        h.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.invited) {
                    h.invited = false;
                    h.followTv.setText("关注");
                    h.followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                    h.followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
                    h.followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
                    h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border));
                } else {
                    h.invited = true;
                    h.followTv.setText("已关注");
                    h.followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    h.followTv.setTextColor(ZR.getColor(R.color.color_999999));
                    h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border_gray));
                }

            }
        });

        ZR.setTopicFollowClickListener(topicResult, h.followButton, h.followTv);
        ZR.showTopicFollowStatus(h.followButton, h.followTv, topicResult.isFollow);
    }

    @Override
    public SearchTopicViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.search_topic_item, viewGroup, false);
        return new SearchTopicViewHolder(view);
    }
/*
    private void showInvited(SearchTopicViewHolder h) {
        if (!h.invited) {
            h.followTv.setText("关注");
            h.followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            h.followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
            h.followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border));
        } else {
            h.followTv.setText("已关注");
            h.followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            h.followTv.setTextColor(ZR.getColor(R.color.color_999999));
            h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border_gray));
        }
    }*/
}
