package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.QuestionActivity;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.server.QuestResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.viewholder.QuestionsViewHolder;

import java.util.ArrayList;
import java.util.List;


public class QuestionsAdapter extends RecyclerView.Adapter {
    public List<QuestResult> mList;
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
        final QuestResult result = mList.get(i);
        final QuestionsViewHolder h = (QuestionsViewHolder) cellHolder;
        h.titleTv.setText(result.title);
        updateFollowButton(result.isFollow, h.followTv);
        h.followTv.setText(ZR.getNumberString(result.tqFollowNum));
        h.answerTv.setText(ZR.getNumberString(result.qanswerNum));

        //hide divider for the last item
        if (i == mList.size() - 1) {
            h.divider.setVisibility(View.GONE);
        }
        h.answerTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                Intent i = new Intent(v.getContext(), QuestionActivity.class);
                i.putExtra(CONST.QUESTION_ID, result.questionId);
                ZGoto.to(i);
            }
        });

        h.followTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final TQuestionParams params = new TQuestionParams();
                params.attentObjId = result.questionId;
                //question
                params.objType = 1;
                //followed
                if (result.isFollow == 1) {
                    params.enabled = 0;
                } else {
                    params.enabled = 1;
                }

                APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        result.isFollow = params.enabled;
                        if (result.isFollow == 1) {
                            result.tqFollowNum = result.tqFollowNum + 1;
                            h.followTv.setText(ZR.getNumberString(result.tqFollowNum));
                            ZToast.toast("关注成功");
                        } else {
                            result.tqFollowNum = result.tqFollowNum - 1;
                            h.followTv.setText(ZR.getNumberString(result.tqFollowNum));
                            ZToast.toast("取消关注");
                        }
                        updateFollowButton(result.isFollow, h.followTv);
                    }
                });
            }
        });
    }

    private void updateFollowButton(int enable, final TextView followTv) {
        //update the follow status
        if (enable == 1) {
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_follow_p, 0, 0, 0);
        } else {
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_follow_n, 0, 0, 0);
        }
    }

    @Override
    public int getItemCount() {
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
}
