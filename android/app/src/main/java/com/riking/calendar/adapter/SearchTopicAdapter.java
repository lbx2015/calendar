package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.ExcellentViewHolderViewHolder;

import java.util.ArrayList;
import java.util.List;


public class SearchTopicAdapter extends RecyclerView.Adapter<ExcellentViewHolderViewHolder> {
    private Context context;
    private List<String> mList;

    public SearchTopicAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public ExcellentViewHolderViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.search_topic_item, viewGroup, false);
        return new ExcellentViewHolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ExcellentViewHolderViewHolder h, int i) {
        h.userName.setText("银民银行数据大集中");
        RequestOptions options = new RequestOptions();
        Glide.with(h.userImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.userImage);

        h.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.invited) {
                    h.invited = false;
                    h.followTv.setText("关注");
                    h.followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                    h.followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
                    h.followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
                    h.followButton.setBackground(h.followButton.getResources().getDrawable(R.drawable.follow_border));
                } else {
                    h.invited = true;
                    h.followTv.setText("已关注");
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

    public void add(String s) {
        mList.add(s);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
