package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.AnswerCommentsActivity;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.QAnswerParams;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.viewholder.HotAnswerOfTopicViewHolder;

import java.util.ArrayList;
import java.util.List;


public class HotAnswerOfTopicAdapter extends ZAdater<HotAnswerOfTopicViewHolder, QAnswerResult> {

    @Override
    public void onBindVH(final HotAnswerOfTopicViewHolder h, int i) {
        final QAnswerResult qAnswerResult = mList.get(i);
        h.answerTitle.setText(qAnswerResult.title);
        h.answerContent.setText(qAnswerResult.content);
        ZR.setUserName(h.answerAuthorName, qAnswerResult.userName, qAnswerResult.grade, qAnswerResult.userId);
        h.agreeTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final QAnswerParams p = new QAnswerParams();
                p.questAnswerId = qAnswerResult.qaId;
                //answer agree type
                p.optType = 1;
                if (qAnswerResult.isAgree == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }
                APIClient.qAnswerAgree(p, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        if (p.enabled == 1) {
                            qAnswerResult.isAgree = 1;
                            qAnswerResult.qaAgreeNum = qAnswerResult.qaAgreeNum + 1;
                            h.agreeTv.setText(ZR.getNumberString(qAnswerResult.qaAgreeNum));
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                            Toast.makeText(h.agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            qAnswerResult.isAgree = 0;
                            qAnswerResult.qaAgreeNum = qAnswerResult.qaAgreeNum - 1;
                            h.agreeTv.setText(ZR.getNumberString(qAnswerResult.qaAgreeNum));
                            ZToast.toast("取消点赞");
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
                        }
                    }
                });
            }
        });

        h.agreeTv.setText(ZR.getNumberString(qAnswerResult.qaAgreeNum));
        if (qAnswerResult.isAgree == 1) {
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
        } else {
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
        }

        h.reviewTv.setText(ZR.getNumberString(qAnswerResult.qaCommentNum));
        h.reviewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(h.reviewTv.getContext(), AnswerCommentsActivity.class);
                i.putExtra(CONST.ANSWER_ID, qAnswerResult.qaId);
                ZGoto.to(i);
            }
        });

        if (StringUtil.isEmpty(qAnswerResult.coverUrl) || !StringUtil.isHttpUrl(qAnswerResult.coverUrl)) {
            h.answerImage.setVisibility(View.GONE);
        } else {
            h.answerImage.setVisibility(View.VISIBLE);
            ZR.setImage(h.answerImage, qAnswerResult.coverUrl);
        }

        //set user image
        ZR.setUserImage(h.authorImage, qAnswerResult.photoUrl);
    }

    @Override
    public HotAnswerOfTopicViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.hot_answer_of_topic_item, viewGroup, false);
        return new HotAnswerOfTopicViewHolder(view);
    }

//    @Override
//    public int getItemCount() {
//        return mList.size();
//    }
//
//    public void setData(List<QAnswerResult> data) {
//        this.mList = data;
//        notifyDataSetChanged();
//    }

//    public void clear() {
//        mList.clear();
//        notifyDataSetChanged();
//    }
}
