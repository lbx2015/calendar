package com.riking.calendar.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ExcellentViewHolderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.summary)
    public TextView summary;
    @BindView(R.id.user_name)
    public TextView userName;
    @BindView(R.id.user_icon)
    public CircleImageView userImage;

    @BindView(R.id.follow_text)
    public TextView followTv;
//    @BindView(R.userId.follow_plus_icon_image)
//    public ImageView followPlusIconImage;
    @BindView(R.id.follow_button)
    public View followButton;

    public boolean invited;

    public ExcellentViewHolderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setTag(this);
    }
}
