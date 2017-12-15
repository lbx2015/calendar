package com.riking.calendar.viewholder;

import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;
import com.riking.calendar.viewholder.base.ZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchTopicViewHolder extends ZViewHolder {

    @BindView(R.id.topic_summary)
    public TextView summary;
    @BindView(R.id.topic_title)
    public TextView title;
    @BindView(R.id.topic_icon)
    public CircleImageView topicImage;

    @BindView(R.id.follow_text)
    public TextView followTv;
    //    @BindView(R.userId.follow_plus_icon_image)
//    public ImageView followPlusIconImage;
    @BindView(R.id.follow_button)
    public View followButton;

    public boolean invited;

    public SearchTopicViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }
}
