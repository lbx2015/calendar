package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.pojo.server.QAComment;
import com.riking.calendar.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

//answer comment adapter
public class MyFollowersAdapter extends RecyclerView.Adapter<MyFollowersAdapter.MyViewHolder> {
    public List<QAComment> mList;

    {
        mList = new ArrayList<>();
    }

    @Override
    public MyFollowersAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.my_follower_item, viewGroup, false);
        return new MyFollowersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyFollowersAdapter.MyViewHolder h, int i) {
        //hide the divider
        if (i == getItemCount() - 1) {
            h.divider.setVisibility(View.GONE);
        } else {
            h.divider.setVisibility(View.VISIBLE);
        }
       /* final QAComment c = mList.get(i);

        h.authorName.setText(c.userName);
        //show time.
        if (c.createdTime != null) {
            h.createTimeTv.setText(DateUtil.showTime(c.createdTime, CONST.yyyy_mm_dd_hh_mm));
        }
        if (c.content != null) {
            h.answerContent.setText(c.content);
        }

        h.agreeTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                final CommentParams p = new CommentParams();
                p.commentId = c.qACommentId;
                //answer reply
                p.objType = 1;

                if (c.isAgree == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }

                APIClient.commentAgree(p, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        if (p.enabled == 1) {
                            c.isAgree = 1;
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                            Toast.makeText(h.agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            c.isAgree = 0;
                            ZToast.toast("取消点赞");
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
                        }
                    }
                });
            }
        });

        if (c.isAgree == 1) {
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
        } else {
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
        }

        RequestOptions options = new RequestOptions();
        Glide.with(h.authorImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.authorImage);
        setRecyclerView(h.recyclerView, h, i);*/
    }

    @Override
    public int getItemCount() {
        return mList.size() + 2;
    }

    public void addAll(List<QAComment> mList) {
        this.mList.clear();
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
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
