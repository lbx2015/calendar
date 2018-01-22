package com.riking.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.CircleImageView;
import com.riking.calendar.viewholder.base.ZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

//answer comment adapter
public class MyFollowersAdapter extends ZAdater<MyFollowersAdapter.MyViewHolder, AppUserResult> {
    @Override
    public void onBindVH(MyViewHolder h, int i) {
        AppUserResult appUser = mList.get(i);
        ZR.showPersonFollowStatus(h.followButton, h.followTv, appUser.isFollow);
        ZR.setFollowPersonClickListner(appUser, h.followButton, h.followTv);
        ZR.setUserName(h.userName, appUser.userName, appUser.userId);
        ZR.setCircleUserImage(h.userIconLayout, appUser.photoUrl, appUser.userId, appUser.grade);
        h.summary.setText(appUser.descript);
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_follower_item, viewGroup, false);
        return new MyFollowersAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends ZViewHolder {
        @BindView(R.id.summary)
        public TextView summary;
        @BindView(R.id.user_name)
        public TextView userName;
        @BindView(R.id.user_icon_layout)
        public FrameLayout userIconLayout;

        @BindView(R.id.follow_text)
        public TextView followTv;
        //    @BindView(R.userId.follow_plus_icon_image)
//    public ImageView followPlusIconImage;
        @BindView(R.id.follow_button)
        public View followButton;
        @BindView(R.id.divider)
        public View divider;

        public boolean invited;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }
    }
}
