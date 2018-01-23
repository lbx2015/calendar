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

//news comment adapter
public class ExcellentPersonAnswerAdapter extends ZAdater<ExcellentPersonAnswerAdapter.MyViewHolder, QAnswerResult> {

    @Override
    public void onBindVH(final MyViewHolder h, int i) {
        final QAnswerResult c = mList.get(i);
        h.questionTitle.setText(c.title);
        h.answerContent.setText(c.content);
        h.time.setText(DateUtil.showTime(c.createdTime));
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.excellent_person_answer_list_item, viewGroup, false);
        return new ExcellentPersonAnswerAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends ZViewHolder {
        public TextView answerContent;
        public TextView questionTitle;
        public TextView time;

        public MyViewHolder(View itemView) {
            super(itemView);
            answerContent = itemView.findViewById(R.id.answer_content_tv);
            questionTitle = itemView.findViewById(R.id.question_title);
            time = itemView.findViewById(R.id.answer_update_time);
        }
    }
}
