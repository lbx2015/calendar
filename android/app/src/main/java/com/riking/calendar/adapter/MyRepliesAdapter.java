package com.riking.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.QAnswerResult;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.viewholder.base.ZViewHolder;

//answer  adapter
public class MyRepliesAdapter extends ZAdater<MyRepliesAdapter.MyViewHolder, QAnswerResult> {
    @Override
    public void onBindViewHolder(final MyRepliesAdapter.MyViewHolder h, int i) {
        //hide the divider
        if (i == getItemCount() - 1) {
            h.divider.setVisibility(View.GONE);
        } else {
            h.divider.setVisibility(View.VISIBLE);
        }
       /* final QAComment c = mList.get(i);

        h.authorName.setText(c.userName);
        //show time.
        if (c.createdTime != null) {
            h.createTimeTv.setText(DateUtil.showTime(c.createdTime, CONST.yyyy_mm_dd_hh_mm));
        }
        if (c.content != null) {
            h.answerContent.setText(c.content);
        }

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
                        if (p.enabled == 1) {
                            c.isAgree = 1;
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                            Toast.makeText(h.agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            c.isAgree = 0;
                            ZToast.toast("取消点赞");
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
                        }
                    }
                });
            }
        });

        if (c.isAgree == 1) {
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
        } else {
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
        }

        RequestOptions options = new RequestOptions();
        Glide.with(h.authorImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.authorImage);
        setRecyclerView(h.recyclerView, h, i);*/
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_repliest_item, viewGroup, false);
        return new MyRepliesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindVH(MyViewHolder h, int i) {
        QAnswerResult result = mList.get(i);
        h.questionTitleTv.setText(result.title);
        h.answerContetnTv.setText(result.content);
        h.replyTimeTv.setText(DateUtil.showTime(result.createdTime));
    }

    class MyViewHolder extends ZViewHolder {
        public TextView questionTitleTv;
        public TextView answerContetnTv;
        public TextView replyTimeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            questionTitleTv = itemView.findViewById(R.id.question_title);
            answerContetnTv = itemView.findViewById(R.id.answer_content);
            replyTimeTv = itemView.findViewById(R.id.reply_time);
        }
    }
}
