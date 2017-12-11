package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.app.GlideApp;
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


public class SearchPersonAdapter extends RecyclerView.Adapter<ExcellentViewHolderViewHolder> {
    private Context context;
    private List<AppUserResult> mList;

    public SearchPersonAdapter(Context context) {
        this.context = context;
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

        h.userName.setText(user.userName);
        h.userName.setCompoundDrawablePadding((int) ZR.convertDpToPx(3));
        h.summary.setText(user.answerNum + "个回答，" + user.agreeNum + "赞");
        h.userName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_icon_grade_v3, 0);
        RequestOptions options = new RequestOptions();
        GlideApp.with(h.userImage.getContext()).load(user.photoUrl)
                .placeholder(R.drawable.default_user_icon)
                .apply(options.fitCenter())
                .into(h.userImage);

        h.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.invited) {
                    h.invited = false;
                } else {
                    h.invited = true;
                }
                showInvited(h);
                sendFollowRequest(user, h);

            }
        });
        showInvited(h);
    }

    private void sendFollowRequest(final AppUserResult user, final ExcellentViewHolderViewHolder h) {
        final TQuestionParams params = new TQuestionParams();
        params.attentObjId = user.userId;
        //follow user
        params.objType = 2;
        //followed
        if (user.isFollow == 1) {
            params.enabled = 0;
        } else {
            params.enabled = 1;
        }

        APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
            @Override
            public void callBack(ResponseModel<String> response) {
                user.isFollow = params.enabled;
                if (user.isFollow == 1) {
                    ZToast.toast("关注成功");
                } else {
                    ZToast.toast("取消关注");
                }
                showInvited(h);
            }
        });
    }

    private void showInvited(ExcellentViewHolderViewHolder h) {
        if (!h.invited) {
            h.followTv.setText("关注");
            h.followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            h.followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
            h.followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border));
        } else {
            h.followTv.setText("已关注");
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
