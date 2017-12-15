package com.riking.calendar.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.AnswerCommentsActivity;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.CommentParams;
import com.riking.calendar.pojo.server.NCReply;
import com.riking.calendar.pojo.server.QAComment;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;
import com.riking.calendar.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

//answer comment adapter
public class AnswerCommentListAdapter extends RecyclerView.Adapter<AnswerCommentListAdapter.MyViewHolder> {
    public List<QAComment> mList;
    private AnswerCommentsActivity a;

    public AnswerCommentListAdapter(AnswerCommentsActivity context) {
        this.a = context;
        mList = new ArrayList<>();
    }

    @Override
    public AnswerCommentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.comment_list_item, viewGroup, false);
        return new AnswerCommentListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnswerCommentListAdapter.MyViewHolder h, int i) {
        final QAComment c = mList.get(i);

        ZR.setUserName(h.authorName, c.userName, c.experience);
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
                        c.isAgree = p.enabled;
                        if (p.enabled == 1) {
                            //agree number plus one
                            c.agreeNumber = c.agreeNumber + 1;
                            h.agreeTv.setText(ZR.getNumberString(c.agreeNumber));
                            h.agreeTv.setTextColor(ZR.getColor(R.color.color_489dfff));
                            h.agreeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_icon_zan_p, 0, 0, 0);
                            Toast.makeText(h.agreeTv.getContext(), "点赞成功", Toast.LENGTH_SHORT).show();
                        } else {
                            ZToast.toast("取消点赞");
                            //agree number minus one
                            c.agreeNumber = c.agreeNumber - 1;
                            h.agreeTv.setText(ZR.getNumberString(c.agreeNumber));
                            h.agreeTv.setTextColor(ZR.getColor(R.color.color_999999));
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

        RequestOptions options = new RequestOptions();
        Glide.with(h.authorImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.authorImage);
        setRecyclerView(h.recyclerView, h, i);
        h.answerContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.clickWriteComment(c, h.replyListAdapter, h.recyclerView);
            }
        });
    }

    private void setRecyclerView(final RecyclerView recyclerView, final AnswerCommentListAdapter.MyViewHolder h, final int position) {
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        h.replyListAdapter = new AnswerReplyListAdapter(a, recyclerView);
        List<NCReply> replies = mList.get(position).qacReplyList;
        MyLog.d("replies number: " + replies + " id : " + mList.get(position).qACommentId);

        if (replies == null || replies.size() == 0) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            h.replyListAdapter.add(replies);
            recyclerView.setAdapter(h.replyListAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
        public CircleImageView authorImage;
        public AnswerReplyListAdapter replyListAdapter;
        public RecyclerView recyclerView;
        public TextView createTimeTv;
        public TextView answerContent;
        public TextView authorName;
        public TextView agreeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            createTimeTv = itemView.findViewById(R.id.answer_update_time);
            authorImage = itemView.findViewById(R.id.answer_author_icon);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            answerContent = itemView.findViewById(R.id.answer_content);
            authorName = itemView.findViewById(R.id.answer_author_name);
            agreeTv = itemView.findViewById(R.id.agree);
        }
    }
}