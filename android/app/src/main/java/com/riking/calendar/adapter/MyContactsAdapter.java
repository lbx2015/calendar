package com.riking.calendar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.server.OtherUserResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.CircleImageView;
import com.riking.calendar.viewholder.base.ZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

//answer comment adapter
public class MyContactsAdapter extends ZAdater<MyContactsAdapter.MyViewHolder, OtherUserResp> {
    @Override
    public void onBindVH(final MyViewHolder h, int i) {
        final OtherUserResp appUser = mList.get(i);
//        未邀请
        if (appUser.isFollow == -2) {
            ZR.showPersonInviteStatus(h.followButton, h.followTv, 0, "邀请");
        } else if (appUser.isFollow == -1) {
            ZR.showPersonInviteStatus(h.followButton, h.followTv, 1, "再次邀请");
        } else {
            ZR.showPersonFollowStatus(h.followButton, h.followTv, appUser.isFollow);
        }
//        ZR.setInviteClickListener(appUser, h.followButton, h.followTv);

        h.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //未注册
                if (appUser.isFollow < 0) {
                    if (h.invited) {
                        ZToast.toast("已经邀请过了");
                        return;
                    }
                    final UserParams params = new UserParams();
                    //follow user
                    params.phone = appUser.phone;

                    APIClient.inviteContact(params, new ZCallBack<ResponseModel<String>>() {
                        @Override
                        public void callBack(ResponseModel<String> response) {
                            appUser.isFollow = -1;
                            h.invited = true;
                            ZR.showPersonInviteStatus(h.followButton, h.followTv, 1, "已邀请");
                        }
                    });
                }
                //已注册
                else {
                    ZR.setFollowPersonClickListner(appUser, h.followButton, h.followTv);
                }
            }
        });
        ZR.setUserName(h.userName, appUser.userName, appUser.grade, appUser.userId);
        h.summary.setVisibility(View.GONE);
//        h.summary.setText(appUser.phone);
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_follower_item, viewGroup, false);
        return new MyContactsAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends ZViewHolder {
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
