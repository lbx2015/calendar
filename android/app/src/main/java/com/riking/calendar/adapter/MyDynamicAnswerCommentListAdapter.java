package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.CommentParams;
import com.riking.calendar.pojo.server.QACommentResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.CircleImageView;
import com.riking.calendar.viewholder.base.ZViewHolder;

import java.util.ArrayList;
import java.util.List;

//answer comment adapter
public class MyDynamicAnswerCommentListAdapter extends ZAdater<MyDynamicAnswerCommentListAdapter.MyViewHolder, QACommentResult> {

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_dynamic_comment_list_item, viewGroup, false);
        return new MyDynamicAnswerCommentListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindVH(final MyDynamicAnswerCommentListAdapter.MyViewHolder h, int i) {
        final QACommentResult c = mList.get(i);

        ZR.setUserName(h.authorName, c.userName, c.grade, c.userId);
        //show time.
        if (c.createdTime != null) {
            h.createTimeTv.setText(DateUtil.showTime(c.createdTime, CONST.yyyy_mm_dd_hh_mm));
        }

        h.answerContent.setText(c.qaContent);
        h.questionTitle.setText(c.tqTitle);

        h.commentContent.setText(c.content);

        h.agreeTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final CommentParams p = new CommentParams();
                p.commentId = c.qACommentId;
                //answer reply
                p.objType = 1;

                if (c.isAgree == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }

                APIClient.commentAgree(p, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        c.isAgree = p.enabled;
                        if (p.enabled == 1) {
                            //agree number plus one
                            c.agreeNumber = c.agreeNumber + 1;
                            h.agreeTv.setText(ZR.getNumberString(c.agreeNumber));
                            h.agreeTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                            Toast.makeText(h.agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            ZToast.toast("取消点赞");
                            //agree number minus one
                            c.agreeNumber = c.agreeNumber - 1;
                            h.agreeTv.setText(ZR.getNumberString(c.agreeNumber));
                            h.agreeTv.setTextColor(ZR.getColor(R.color.color_999999));
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
                        }
                    }
                });
            }
        });

        //set the agree number
        h.agreeTv.setText(ZR.getNumberString(c.agreeNumber));

        if (c.isAgree == 1) {
            h.agreeTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
        } else {
            h.agreeTv.setTextColor(ZR.getColor(R.color.color_999999));
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
        }

        ZR.setUserImage(h.authorImage, c.photoUrl);
    }

/*
    public void addAll(List<QACommentResult> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }
*/

    class MyViewHolder extends ZViewHolder {
        public CircleImageView authorImage;
        public AnswerReplyListAdapter replyListAdapter;
        public RecyclerView recyclerView;
        public TextView createTimeTv;
        public TextView commentContent;
        public TextView answerContent;
        public TextView authorName;
        public TextView agreeTv;
        public TextView questionTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            questionTitle = itemView.findViewById(R.id.question_title);
            createTimeTv = itemView.findViewById(R.id.update_time);
            authorImage = itemView.findViewById(R.id.author_icon);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            answerContent = itemView.findViewById(R.id.answer_content);
            authorName = itemView.findViewById(R.id.author_name);
            commentContent = itemView.findViewById(R.id.comment_content);
            agreeTv = itemView.findViewById(R.id.agree);
        }
    }
}