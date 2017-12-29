package com.riking.calendar.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.AnswerCommentsActivity;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.QAnswerParams;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.pojo.server.TQuestionResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.CircleImageView;
import com.riking.calendar.viewholder.HomeViewHolder;
import com.riking.calendar.viewholder.base.ZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

//answer comment adapter
public class MyCollectAnswerAdapter extends ZAdater<MyCollectAnswerAdapter.MyViewHolder, QAnswerResult> {
    @Override
    public void onBindVH(MyViewHolder h, int i) {
        QAnswerResult answer = mList.get(i);
        ZR.setUserName(h.itemCator, answer.userName, answer.grade);
        ZR.setUserImage(h.fromImage, answer.photoUrl);
        h.questionTitle.setText(answer.title);
        h.answerContent.setText(answer.content);
        h.timeTv.setText(DateUtil.showTime(answer.createdTime));
        setAnswerAgreeAndComment(h, answer);
    }

    private void setAnswerAgreeAndComment(final MyViewHolder h, final QAnswerResult r) {
        //set the answer comment number
        h.firstTextIcon.setText(ZR.getNumberString(r.qaCommentNum));
        //set the answer agree number
        h.secondTextIcon.setText(ZR.getNumberString(r.qaAgreeNum));

        if (r.isAgree == 1) {
            h.secondTextIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
        } else {
            h.secondTextIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
        }

        h.firstTextIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_comment, 0, 0, 0);

        //set agree listener
        setAgreeClick(h.secondTextIcon, r);

        //go to comment list activity on cick
        h.firstTextIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(h.firstTextIcon.getContext(), AnswerCommentsActivity.class);
                i.putExtra(CONST.ANSWER_ID, r.qaId);
                i.putExtra(CONST.ANSWER_COMMENT_NUM, r.qaCommentNum);
                ZGoto.to(i);
            }
        });
    }


    private void setAgreeClick(final TextView agreeTv, final QAnswerResult r) {
        agreeTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final QAnswerParams p = new QAnswerParams();
                p.questAnswerId = r.qaId;
                //answer agree type
                p.optType = 1;
                if (r.isAgree == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }
                APIClient.qAnswerAgree(p, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        if (p.enabled == 1) {
                            r.isAgree = 1;
                            r.qaAgreeNum = r.qaAgreeNum + 1;
                            agreeTv.setText(ZR.getNumberString(r.qaAgreeNum));
                            agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                            Toast.makeText(agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            r.isAgree = 0;
                            r.qaAgreeNum = r.qaAgreeNum - 1;
                            agreeTv.setText(ZR.getNumberString(r.qaAgreeNum));
                            ZToast.toast("取消点赞");
                            agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
                        }
                    }
                });
            }
        });
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_collect_answer_item, viewGroup, false);
        return new MyCollectAnswerAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends ZViewHolder {
        @BindView(R.id.item_cator)
        public TextView itemCator;
        @BindView(R.id.answer_content)
        public TextView answerContent;
        @BindView(R.id.answer_image)
        public ImageView answerImage;
        @BindView(R.id.from_image)
        public CircleImageView fromImage;

        @BindView(R.id.answer_title)
        public TextView questionTitle;
        @BindView(R.id.review_number)
        public TextView firstTextIcon;
        @BindView(R.id.agree_number)
        public TextView secondTextIcon;
        @BindView(R.id.time)
        public TextView timeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }
    }
}
