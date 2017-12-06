package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.CommentsActivity;
import com.riking.calendar.pojo.server.NCReply;
import com.riking.calendar.util.ZR;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReplyListAdapter extends RecyclerView.Adapter<ReplyListAdapter.MyViewHolder> {
    CommentsActivity a;
    String from;
    private List<NCReply> mList;

    public ReplyListAdapter(CommentsActivity c) {
        a = c;
        mList = new ArrayList<>();
    }

    @Override
    public ReplyListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.reply_list_item, viewGroup, false);
        return new ReplyListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReplyListAdapter.MyViewHolder h, int i) {
        NCReply reply = mList.get(i);
        from = reply.userName == null ? "周恩来" : reply.userName;
        String to = reply.toUserName == null ? "毛泽东" : reply.toUserName;
        String replyText = "回复";
        String content = reply.content == null ? "总理还是我们的总理呀。总理还是我们的总理呀。" : reply.content;

        SpannableString fromSpan = new SpannableString(from);
        SpannableString replySpan = new SpannableString(replyText);
        SpannableString toSpan = null;
        if (!TextUtils.isEmpty(to)) {
            toSpan = new SpannableString(to);
        }
        SpannableString contentSpan = new SpannableString(content);

        ClickableSpan fromClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                a.clickWriteComment(from);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ZR.getColor(R.color.color_489dfff));
                ds.setTextSize(ZR.convertDpToPx(15));
//                ds.setUnderlineText(false);
            }
        };

        ClickableSpan replyClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                a.clickWriteComment(from);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ZR.getColor(R.color.color_222222));
                ds.setTextSize(ZR.convertDpToPx(15));
//                ds.setUnderlineText(false);
            }
        };

        ClickableSpan toClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                a.clickWriteComment(from);

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ZR.getColor(R.color.color_489dfff));
                ds.setTextSize(ZR.convertDpToPx(15));
//                ds.setUnderlineText(false);
            }
        };


        ClickableSpan contentClick = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                a.clickWriteComment(from);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ZR.getColor(R.color.color_222222));
                ds.setTextSize(ZR.convertDpToPx(15));
//                ds.setUnderlineText(false);
            }
        };


        fromSpan.setSpan(fromClick, 0, from.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        if (toSpan != null) {
            toSpan.setSpan(toClick, 0, to.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        replySpan.setSpan(replyClick, 0, replyText.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        contentSpan.setSpan(contentClick, 0, content.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        Spanned richText = null;
        if (toSpan == null) {
            richText = (Spanned) android.text.TextUtils.concat(fromSpan, contentSpan);
        } else {
            richText = (Spanned) android.text.TextUtils.concat(fromSpan, replySpan, toSpan, contentSpan);
        }
        h.replyTv.setText(richText);
        h.replyTv.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(List<NCReply> s) {
        if (s != null) {
            MyLog.d("add reply list");
            mList.clear();
            mList = s;
            notifyDataSetChanged();
        }
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.reply_tv)
        TextView replyTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
