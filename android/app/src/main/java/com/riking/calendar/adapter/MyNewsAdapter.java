package com.riking.calendar.adapter;

import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.UserActivity;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.SysNoticeResult;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.base.ZViewHolder;

//answer comment adapter
public class MyNewsAdapter extends ZAdater<MyNewsAdapter.MyViewHolder, SysNoticeResult> {
    public boolean editMode;
    public boolean selectAll;

    @Override
    public void onBindVH(final MyViewHolder h, int i) {
        final SysNoticeResult result = mList.get(i);
        if (editMode) {
            h.checkImage.setVisibility(View.VISIBLE);
            h.checkImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (h.checked) {
                        h.checked = false;
                        h.checkImage.setImageDrawable(ZR.getDrawable(R.drawable.mess_icon_editdelete_n));
//                        ZR.setImage(h.checkImage, R.drawable.mess_icon_editdelete_n);
                    } else {
                        h.checkImage.setImageDrawable(ZR.getDrawable(R.drawable.mess_icon_editdelete_s));
//                        ZR.setImage(h.checkImage, R.drawable.mess_icon_editdelete_s);
                        h.checked = true;
                    }
                }
            });
        } else {
            h.checkImage.setVisibility(View.GONE);
        }

        if (selectAll) {
            h.checked = true;
            ZR.setImage(h.checkImage, R.drawable.mess_icon_editdelete_s);
        } else {
            h.checked = false;
            ZR.setImage(h.checkImage, R.drawable.mess_icon_editdelete_n);
        }
        MyLog.d("result : " + result.toString());
        //system info
        if (result.dataType == 0) {
            h.title.setTextSize(17);
            h.title.setText("系统消息");
            h.systemNewsTv.setText(result.content);
            h.systemNewsTv.setVisibility(View.VISIBLE);
            h.userImage.setImageDrawable(ZR.getDrawable(R.drawable.message_icon_systeminfor));
        } else {
            h.systemNewsTv.setVisibility(View.GONE);
            h.title.setMaxLines(2);
            h.title.setEllipsize(TextUtils.TruncateAt.END);
            h.title.setText(Html.fromHtml("<html><body><font color=#666666>" + result.title + " </font> <font color=#333333>" + (result.dataType == 5 ? "" : result.content) + " </font> </body><html>"));
            ZR.setCircleUserImage(h.userImage, result.userPhotoUrl, result.userId);
            if (result.dataType == 2 || result.dataType == 3) {
                ZR.setAnswerClickListener(h.title, result.objId);
            } else if (result.dataType == 1 || result.dataType == 4) {
                ZR.setRequestClickListener(h.title, result.objId);
            } else if (result.dataType == 5) {
                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), UserActivity.class);
                        i.putExtra(CONST.USER_ID, result.objId);
                        ZGoto.to(i);
                    }
                });
            } else if (result.dataType == 6 || result.dataType == 7 || result.dataType == 8) {
                ZGoto.goToAnswerComments(result.objId);
            }
        }

        h.timeTv.setText(DateUtil.showTime(result.createdTime));
     /*   AppUserResult appUser = mList.get(i);
        ZR.showPersonFollowStatus(h.followButton, h.followTv, appUser.isFollow);
        ZR.setFollowPersonClickListner(appUser, h.followButton, h.followTv);
        ZR.setUserName(h.userName, appUser.userName, appUser.grade, appUser.userId);
        h.summary.setText(appUser.descript);*/
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_news_item, viewGroup, false);
        return new MyNewsAdapter.MyViewHolder(view);
    }

    class MyViewHolder extends ZViewHolder {
        public TextView timeTv;
        public TextView title;
        public ImageView userImage;
        public ImageView checkImage;
        public TextView systemNewsTv;
        public boolean checked;

        public MyViewHolder(View itemView) {
            super(itemView);
            checkImage = itemView.findViewById(R.id.check_image);
            userImage = itemView.findViewById(R.id.user_icon);
            title = itemView.findViewById(R.id.user_name);
            timeTv = itemView.findViewById(R.id.summary);
            systemNewsTv = itemView.findViewById(R.id.system_news_tv);
        }
    }
}
