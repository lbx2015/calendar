package com.riking.calendar.adapter;

import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.util.ZR;
import com.riking.calendar.view.CircleImageView;
import com.riking.calendar.viewholder.base.ZViewHolder;

//answer comment adapter
public class MyNewsAdapter extends ZAdater<MyNewsAdapter.MyViewHolder, AppUserResult> {
    public boolean editMode;
    public boolean selectAll;

    @Override
    public void onBindVH(final MyViewHolder h, int i) {
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

        if (i == 4) {
            h.userName.setTextSize(17);
            h.userName.setText("系统消息");
            h.systemNewsTv.setText("亲爱的用户，欢迎来到App，我们为您准时推送。");
            h.systemNewsTv.setVisibility(View.VISIBLE);
            h.userImage.setImageDrawable(ZR.getDrawable(R.drawable.message_icon_systeminfor));
        } else {
            h.systemNewsTv.setVisibility(View.GONE);
            h.userName.setMaxLines(2);
            h.userName.setEllipsize(TextUtils.TruncateAt.END);
            String s = "周云丽 回答了你的问题";
            String content = "同业存单尽管属于同业投资，在会计上需要单独设计科目管理。";
            h.userName.setText(Html.fromHtml("<html><body><font color=#666666>" + s + " </font> <font color=#333333>" + content + " </font> </body><html>"));
        }
        h.summary.setText("30分钟前");
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

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class MyViewHolder extends ZViewHolder {
        public TextView summary;
        public TextView userName;
        public ImageView userImage;
        public ImageView checkImage;
        public TextView systemNewsTv;
        public boolean checked;

        public MyViewHolder(View itemView) {
            super(itemView);
            checkImage = itemView.findViewById(R.id.check_image);
            userImage = itemView.findViewById(R.id.user_icon);
            userName = itemView.findViewById(R.id.user_name);
            summary = itemView.findViewById(R.id.summary);
            systemNewsTv = itemView.findViewById(R.id.system_news_tv);
        }
    }
}
