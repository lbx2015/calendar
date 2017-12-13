package com.riking.calendar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/10/11.
 */

public class QuestionsViewHolder extends RecyclerView.ViewHolder {
    public TextView titleTv;
    public TextView followTv;
    public TextView answerTv;

    public QuestionsViewHolder(View v) {
        super(v);
        titleTv = v.findViewById(R.id.question_title);
        followTv = v.findViewById(R.id.follow_tv);
        answerTv = v.findViewById(R.id.answer_tv);
    }
}
