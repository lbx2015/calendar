package com.riking.calendar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnswerListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.answer_author_name)
    public TextView answerAuthorName;
    @BindView(R.id.answer_author_icon)
    public CircleImageView authorImage;
    @BindView(R.id.answer_content)
    public TextView answerContent;

    public AnswerListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }

    private void resetView() {

    }
}
