package com.riking.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.base.ZUserBaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

//answer comment adapter
public class MyFollowersAdapter extends ZAdater<ZUserBaseViewHolder, AppUserResult> {
    @Override
    public void onBindVH(ZUserBaseViewHolder h, int i) {
        AppUserResult appUser = mList.get(i);
        ZR.showPersonFollowStatus(h.followButton, h.followTv, appUser.isFollow);
        ZR.setFollowPersonClickListner(appUser, h.followButton, h.followTv);
        ZR.setUserInfo(h.userIconLayout, appUser.photoUrl, appUser.userName, appUser.descript, appUser.userId, appUser.grade);
    }

    @Override
    public ZUserBaseViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_follower_item, viewGroup, false);
        return new ZUserBaseViewHolder(view);
    }
}
