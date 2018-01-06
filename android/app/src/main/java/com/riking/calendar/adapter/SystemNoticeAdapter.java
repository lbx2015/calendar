package com.riking.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.SysNoticeResult;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.viewholder.base.ZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

//answer comment adapter
public class SystemNoticeAdapter extends ZAdater<SystemNoticeAdapter.MyViewHolder, SysNoticeResult> {
    @Override
    public void onBindVH(MyViewHolder h, int i) {
        SysNoticeResult r = mList.get(i);
        h.contentTv.setText(r.content);
        h.timeTv.setText(DateUtil.showTime(r.createdTime));
//        ZR.showPersonFollowStatus(h.followButton, h.followTv, appUser.isFollow);
//        ZR.setFollowPersonClickListner(appUser, h.followButton, h.followTv);
//        ZR.setUserName(h.userName, appUser.userName, appUser.grade,appUser.userId);
//        h.summary.setText(appUser.descript);
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.system_notice_item, viewGroup, false);
        return new SystemNoticeAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends ZViewHolder {
        @BindView(R.id.content_tv)
        public TextView contentTv;
        @BindView(R.id.time_iv)
        public TextView timeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }
    }
}
