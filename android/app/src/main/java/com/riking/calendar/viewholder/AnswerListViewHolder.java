package com.riking.calendar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xiangzhihong on 2016/3/2 on 15:49.
 */
public class HotAnswerOfTopicViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.answer_author_name)
    public TextView answerAuthorName;
    @BindView(R.id.answer_author_icon)
    public CircleImageView authorImage;
    @BindView(R.id.answer_content)
    public TextView answerContent;

    public HotAnswerOfTopicViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }

    private void resetView() {

    }
}
