package com.riking.calendar.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.UserActivity;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.CircleImageView;

import java.util.List;

/**
 * Created by zw.zhang on 2017/10/11.
 */

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.RecommendedViewHolder> {
    //    @Comment("可能感兴趣的话题")
    public List<Topic> topicResults;

    //    @Comment("可能感兴趣的人")
    public List<AppUserResult> appUserResults;

    public int type;

    @Override
    public RecommendedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_row, parent, false);
        return new RecommendedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecommendedViewHolder holder, int position) {
        switch (type) {
            //topic
            case 6: {
                final Topic topicResult = topicResults.get(position);
                setFollowClickListener(holder, topicResult);
                ZR.setImage(holder.iconImage, topicResult.topicUrl);
                holder.recommendedTv.setText(topicResult.title);
                ZR.setTopicName(holder.recommendedTv, topicResult.title, topicResult.topicId);
//                holder.iconImage.setOnClickListener(onClickListener);
                ZR.setCircleTopicImage(holder.iconImage, topicResult.topicUrl, topicResult.topicId);
                break;
            }

            //person
            case 7: {
                final AppUserResult user = appUserResults.get(position);
                ZR.setUserName(holder.recommendedTv, user.userName, user.grade, user.userId);
                ZR.setFollowPersonClickListner(user, holder.followButton, holder.followTv);
                ZR.showPersonFollowStatus(holder.followButton, holder.followTv, user.isFollow);
                ZR.setCircleUserImage(holder.iconImage, user.photoUrl, user.userId);
                //go to topic on click
                holder.recommendedTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //go to topic detail on click
                        Intent i = new Intent(holder.recommendedTv.getContext(), UserActivity.class);
                        i.putExtra(CONST.USER_ID, user.userId);
                        ZGoto.to(i);
                    }
                });
                break;
            }
        }


        if (position == 0) {
            //adding protection
            if (holder.itemView instanceof RelativeLayout) {
                RelativeLayout root = (RelativeLayout) holder.itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) root.getLayoutParams();
                layoutParams.leftMargin = (int) ZR.convertDpToPx(15);
            }
        } else if (position == (getItemCount() - 1)) {
            RelativeLayout root = (RelativeLayout) holder.itemView;
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) root.getLayoutParams();
            layoutParams.rightMargin = (int) ZR.convertDpToPx(15);
        } else {
            if (holder.itemView instanceof RelativeLayout) {
                RelativeLayout root = (RelativeLayout) holder.itemView;
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) root.getLayoutParams();
                layoutParams.leftMargin = (int) ZR.convertDpToPx(6);
            }
        }
    }

    private void setFollowClickListener(final RecommendedViewHolder h, final Topic topic) {
        h.followButton.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                //adding null protection
                if (topic == null) {
                    return;
                }
                final TQuestionParams params = new TQuestionParams();
                params.attentObjId = topic.topicId;
                //topic
                params.objType = 2;
                //followed
                if (topic.isFollow == 1) {
                    params.enabled = 0;
                } else {
                    params.enabled = 1;
                }

                APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        topic.isFollow = params.enabled;
                        if (topic.isFollow == 1) {
                            ZToast.toast("关注成功");
                        } else {
                            ZToast.toast("取消关注");
                        }
                        updateFollowButton(h, topic.isFollow);
                    }
                });
            }
        });
    }

    private void updateFollowButton(final RecommendedViewHolder h, final int isFollow) {
        //followed
        if (isFollow == 1) {
            h.followTv.setText("已关注");
            h.followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            h.followTv.setTextColor(ZR.getColor(R.color.color_999999));
            h.followButton.setBackground(ZR.getDrawable(R.drawable.follow_border_gray));
        } else {
            h.followTv.setText("关注");
            h.followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            h.followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
            h.followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            h.followButton.setBackground(ZR.getDrawable(R.drawable.follow_border));
        }
    }

    @Override
    public int getItemCount() {

        switch (type) {
            case 6: {
                return topicResults == null ? 0 : topicResults.size();
            }

            case 7: {
                return appUserResults == null ? 0 : appUserResults.size();
            }
        }
        return 0;
    }

    public class RecommendedViewHolder extends RecyclerView.ViewHolder {
        public View followButton;
        public TextView followTv;
        RelativeLayout root;
        CircleImageView iconImage;
        TextView recommendedTv;

        public RecommendedViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.root);
            iconImage = itemView.findViewById(R.id.icon_image);
            recommendedTv = itemView.findViewById(R.id.recommend_title);
            followButton = itemView.findViewById(R.id.follow_button);
            followTv = itemView.findViewById(R.id.follow_text);
        }
    }
}
