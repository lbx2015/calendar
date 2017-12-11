package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.pojo.server.TopicResult;
import com.riking.calendar.util.ZR;
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
        h.userName.setText("周润发");
        h.userName.setCompoundDrawablePadding((int) ZR.convertDpToPx(3));
        h.answerSummary.setText("200个回答，1360赞");
        h.userName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_icon_grade_v3, 0);
        RequestOptions options = new RequestOptions();
        Glide.with(h.userImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.userImage);

        h.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.invited) {
                    h.invited = false;
                    h.followTv.setText("邀请");
                    h.followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                    h.followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
                    h.followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
                    h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border));
                } else {
                    h.invited = true;
                    h.followTv.setText("已邀请");
                    h.followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    h.followTv.setTextColor(ZR.getColor(R.color.color_999999));
                    h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border_gray));
                }

            }
        });
        showInvited(h);
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
