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
