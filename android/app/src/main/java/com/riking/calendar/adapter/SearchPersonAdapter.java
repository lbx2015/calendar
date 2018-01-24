package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.ExcellentViewHolderViewHolder;
import com.riking.calendar.viewholder.base.ZUserBaseViewHolder;

import java.util.ArrayList;
import java.util.List;


public class SearchPersonAdapter extends ZAdater<ZUserBaseViewHolder,AppUserResult> {
    private Context context;

    public SearchPersonAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindVH(ZUserBaseViewHolder h, int i) {
        final AppUserResult user = mList.get(i);
        ZR.setUserInfo(h.userIconLayout,user.photoUrl,user.userName,user.answerNum + "个回答，" + user.agreeNum + "赞",user.userId,user.grade);
        ZR.setFollowPersonClickListner(user, h.followButton, h.followTv);
        ZR.showPersonFollowStatus(h.followButton, h.followTv, user.isFollow);
    }

    @Override
    public ZUserBaseViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.excellent_answerer_item, viewGroup, false);
        return new ZUserBaseViewHolder(view);
    }
}
