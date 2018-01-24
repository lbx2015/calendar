package com.riking.calendar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.viewholder.base.ZUserBaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnswerListViewHolder extends ZUserBaseViewHolder {

    @BindView(R.id.answer_content)
    public TextView answerContent;
    @BindView(R.id.review_number)
    public TextView commentTV;
    @BindView(R.id.agree_number)
    public TextView agreeTv;

    public AnswerListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }

    private void resetView() {

    }
}
