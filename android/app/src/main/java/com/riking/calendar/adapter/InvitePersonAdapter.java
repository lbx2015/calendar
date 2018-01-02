package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.InvitePersonActivity;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.viewholder.ExcellentViewHolderViewHolder;

import java.util.ArrayList;
import java.util.List;


public class InvitePersonAdapter extends RecyclerView.Adapter<ExcellentViewHolderViewHolder> {
    InvitePersonActivity a;
    private List<AppUserResult> mList;

    public InvitePersonAdapter(InvitePersonActivity context) {
        this.a = context;
        mList = new ArrayList<>();
    }

    @Override
    public ExcellentViewHolderViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.excellent_answerer_item, viewGroup, false);
        return new ExcellentViewHolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExcellentViewHolderViewHolder h, int i) {
        final AppUserResult user = mList.get(i);
        h.userName.setCompoundDrawablePadding((int) ZR.convertDpToPx(3));
        h.summary.setText(user.answerNum + "个回答，" + user.agreeNum + "赞");
        //set user name
        ZR.setUserName(h.userName, user.userName, user.grade,user.userId);

        //set user image
        ZR.setUserImage(h.userImage, user.photoUrl);

        h.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.invited) {
                    h.invited = false;
                } else {
                    h.invited = true;
                }
                sendInviteRequest(user, h);
            }
        });

        h.invited = user.isInvited == 1;
        showInvited(h);
    }

    private void sendInviteRequest(final AppUserResult user, final ExcellentViewHolderViewHolder h) {
        final TQuestionParams params = new TQuestionParams();
        params.tqId = a.questionId;
        params.attentObjId = user.userId;

        APIClient.answerInvite(params, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                user.isInvited = params.enabled;
                if (user.isInvited == 1) {
                    ZToast.toast("邀请成功");
                }
                showInvited(h);
            }
        });
    }

    private void showInvited(ExcellentViewHolderViewHolder h) {
        if (!h.invited) {
            h.followTv.setText("邀请");
            h.followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            h.followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
            h.followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border));
        } else {
            h.followTv.setText("已邀请");
            h.followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            h.followTv.setTextColor(ZR.getColor(R.color.color_999999));
            h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border_gray));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<AppUserResult> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
