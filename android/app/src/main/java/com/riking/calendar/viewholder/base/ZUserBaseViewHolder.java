package com.riking.calendar.viewholder.base;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riking.calendar.R;

/**
 * Created by zw.zhang on 2017/12/15.
 */

public class ZUserBaseViewHolder extends ZViewHolder {
    public TextView followTv;
    public View followButton;
    public RelativeLayout userIconLayout;
    public boolean invited;

    public ZUserBaseViewHolder(View itemView) {
        super(itemView);
        followTv = itemView.findViewById(R.id.follow_text);
        followButton = itemView.findViewById(R.id.follow_button);
        userIconLayout = itemView.findViewById(R.id.user_info_layout);
    }
}
