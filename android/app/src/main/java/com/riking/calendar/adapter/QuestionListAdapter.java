package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.AnswerCommentsActivity;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.QAnswerParams;
import com.riking.calendar.pojo.server.QuestionAnswer;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
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
    public void onBindViewHolder(final AnswerListViewHolder h, int i) {
        final QuestionAnswer questionAnswer = mList.get(i);
        //set author name
        ZR.setUserName(h.answerAuthorName, questionAnswer.userName, questionAnswer.grade,questionAnswer.userId);

        h.answerContent.setText(questionAnswer.content);
        ZR.setAnswerClickListener(h.answerContent,questionAnswer.questionAnswerId);

        h.agreeTv.setText(ZR.getNumberString(questionAnswer.agreeNum));
        h.commentTV.setText(ZR.getNumberString(questionAnswer.commentNum));
        h.commentTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(h.commentTV.getContext(), AnswerCommentsActivity.class);
                i.putExtra(CONST.QUESTION_ID, questionAnswer.questionAnswerId);
                ZGoto.to(i);
            }
        });

        if (questionAnswer.isAgree == 1) {
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
        } else {
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
        }

        h.agreeTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final QAnswerParams p = new QAnswerParams();
                p.questAnswerId = questionAnswer.questionAnswerId;
                //answer agree type
                p.optType = 1;
                if (questionAnswer.isAgree == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }
                APIClient.qAnswerAgree(p, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        if (p.enabled == 1) {
                            questionAnswer.isAgree = 1;
                            questionAnswer.agreeNum = questionAnswer.agreeNum + 1;
                            h.agreeTv.setText(ZR.getNumberString(questionAnswer.agreeNum));
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                            Toast.makeText(h.agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            questionAnswer.isAgree = 0;
                            questionAnswer.agreeNum = questionAnswer.agreeNum - 1;
                            h.agreeTv.setText(ZR.getNumberString(questionAnswer.agreeNum));
                            ZToast.toast("取消点赞");
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
                        }
                    }
                });
            }
        });

        //set user image
        ZR.setUserImage(h.authorImage, questionAnswer.photoUrl);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<QuestionAnswer> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
