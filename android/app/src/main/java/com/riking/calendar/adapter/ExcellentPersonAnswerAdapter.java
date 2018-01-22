package com.riking.calendar.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.CommentsActivity;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.CommentParams;
import com.riking.calendar.pojo.server.NCReply;
import com.riking.calendar.pojo.server.NewsComment;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.viewholder.base.ZViewHolder;

import java.util.List;

//news comment adapter
public class ExcellentPersonAnswerAdapter extends ZAdater<ExcellentPersonAnswerAdapter.MyViewHolder, NewsComment> {

    @Override
    public void onBindVH(final MyViewHolder h, int i) {
        final NewsComment c = mList.get(i);
        ZR.setUserName(h.authorName, c.userName, c.userId);
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
                p.commentId = c.newsCommentId;
                p.objType = 2;
                if (c.isAgree == 1) {
                    p.enabled = 0;
                } else {
                    p.enabled = 1;
                }
                p.userId = ZPreference.pref.getString(CONST.USER_ID, "");
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

        //set the agree number
        h.agreeTv.setText(ZR.getNumberString(c.agreeNumber));

        if (c.isAgree == 1) {
            h.agreeTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
        } else {
            h.agreeTv.setTextColor(ZR.getColor(R.color.color_999999));
            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_n, 0, 0, 0);
        }

        //set the user image icon
        ZR.setCircleUserImage(h.userIconLayout,c.photoUrl,c.userId,c.grade);

        setRecyclerView(h.recyclerView, h, i);

    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.comment_list_item, viewGroup, false);
        return new ExcellentPersonAnswerAdapter.MyViewHolder(view);
    }

    private void setRecyclerView(final RecyclerView recyclerView, final ExcellentPersonAnswerAdapter.MyViewHolder h, final int position) {
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        List<NCReply> replies = mList.get(position).ncReplyList;
        if (replies == null || replies.size() == 0) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            h.replyListAdapter.add(replies);
            recyclerView.setAdapter(h.replyListAdapter);
        }
    }

    class MyViewHolder extends ZViewHolder {
        public FrameLayout userIconLayout;
        public ReplyListAdapter replyListAdapter;
        public RecyclerView recyclerView;
        public TextView createTimeTv;
        public TextView answerContent;
        public TextView authorName;
        public TextView agreeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            createTimeTv = itemView.findViewById(R.id.answer_update_time);
            userIconLayout = itemView.findViewById(R.id.user_icon_layout);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            answerContent = itemView.findViewById(R.id.answer_content);
            authorName = itemView.findViewById(R.id.answer_author_name);
            agreeTv = itemView.findViewById(R.id.agree);
        }
    }
}
