package com.riking.calendar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by xiangzhihong on 2016/3/2 on 15:49.
 */
public class HomeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.item_cator)
    public TextView itemCator;
    @BindView(R.id.answer_content)
    public TextView answerContent;
    @BindView(R.id.answer_image)
    public ImageView answerImage;
    @BindView(R.id.from_image)
    public CircleImageView fromImage;

    @BindView(R.id.more_action)
    public View moreAction;
    @BindView(R.id.answer_title)
    public TextView answerTitle;
    @BindView(R.id.review_number)
    public TextView reviewTv;
    @BindView(R.id.agree_number)
    public TextView agreeTv;

    public HomeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }

    private void resetView() {

    }
}
