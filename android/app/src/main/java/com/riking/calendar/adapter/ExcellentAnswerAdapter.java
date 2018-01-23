package com.riking.calendar.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.ExcellentPersonActivity;
import com.riking.calendar.fragment.ExcellentAnswererFragment;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.server.QAExcellentResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.ExcellentViewHolderViewHolder;

import java.util.ArrayList;
import java.util.List;


public class ExcellentAnswerAdapter extends RecyclerView.Adapter<ExcellentViewHolderViewHolder> {
    private ExcellentAnswererFragment fragment;
    private List<QAExcellentResp> mList;

    public ExcellentAnswerAdapter(ExcellentAnswererFragment fragment) {
        this.fragment = fragment;
        mList = new ArrayList<>();
    }

    @Override
    public ExcellentViewHolderViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.excellent_answerer_item, viewGroup, false);
        return new ExcellentViewHolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExcellentViewHolderViewHolder h, int i) {
        final QAExcellentResp user = mList.get(i);
        h.summary.setText("此话题下" + user.qanswerNum + "个回答，" + user.qaAgreeNum + "赞");
        h.summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(h.summary.getContext(), ExcellentPersonActivity.class);
                i.putExtra(CONST.TOPIC_ID, fragment.activity.topicId);
                i.putExtra(CONST.USER_ID, user.userId);
                i.putExtra(CONST.USER_NAME, user.userName);
                ZGoto.to(i);
            }
        });

        //hide divider for the last item
        if (i == mList.size() - 1) {
            h.divider.setVisibility(View.GONE);
        }

        ZR.setUserName(h.userName, user.userName, user.grade, user.userId);

        ZR.setCircleUserImage(h.userImage, user.photoUrl,user.userId);

        h.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TQuestionParams params = new TQuestionParams();
                params.attentObjId = user.userId;
                //follow person
                params.objType = 3;
                //followed
                if (user.isFollow == 1) {
                    params.enabled = 0;
                } else {
                    params.enabled = 1;
                }

                APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        String state = response._data;
                        if (state == null) {
                            return;
                        }

                        user.isFollow = Integer.valueOf(state);
                        showInvited(h.followButton, h.followTv, user.isFollow);
                    }
                });
            }
        });
        showInvited(h.followButton, h.followTv, user.isFollow);
    }

    private void showInvited(View followButton, TextView followTv, int isFollow) {
        if (isFollow == 0) {
            followTv.setText("关注");
            followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
            followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border));
        } else {
            followTv.setText("已关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            followTv.setTextColor(ZR.getColor(R.color.color_999999));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border_gray));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<QAExcellentResp> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
