package com.riking.calendar.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.NewsDetailActivity;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.News;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.base.ZViewHolder;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class NewsAdapter extends ZAdater<NewsAdapter.MyViewHolder, News> {

    @Override
    public void onBindVH(final MyViewHolder holder, int position) {
        final News news = mList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(holder.itemView.getContext(), NewsDetailActivity.class);
                i.putExtra(CONST.NEWS_ID, news.newsId);
                MyLog.d("click newsId " + news.newsId);
                ZGoto.to(i);
            }
        });
        holder.newsTitle.setText(news.title);
        holder.newsCommentsNoTv.setText(ZR.getNumberString(news.commentNumber) + "评论");
        holder.newsIssuedFromTv.setText(news.issued);
        holder.newsPublishTime.setText(DateUtil.showTime(news.createdTime, CONST.yyyy_mm_dd_hh_mm));
        String[] imageUrls = news.coverUrls.split(";");
        if (!StringUtil.isEmpty(imageUrls[0])) {
            holder.newsImage1.setVisibility(View.VISIBLE);
            ZR.setImage(holder.newsImage1, imageUrls[0]);
        } else {
            holder.newsImage1.setVisibility(View.GONE);
        }

        switch (getItemViewType(position)) {
            case 0: {
                holder.newsTitle.setText(news.title);
                if (!StringUtil.isEmpty(imageUrls[1])) {
                    holder.newsImage2.setVisibility(View.VISIBLE);
                    ZR.setImage(holder.newsImage1, imageUrls[1]);
                } else {
                    holder.newsImage2.setVisibility(View.GONE);
                }
                if (!StringUtil.isEmpty(imageUrls[2])) {
                    holder.newsImage3.setVisibility(View.VISIBLE);
                    ZR.setImage(holder.newsImage1, imageUrls[2]);
                } else {
                    holder.newsImage3.setVisibility(View.GONE);
                }
                break;
            }
            case 1: {
                holder.newsTitle.setText(news.title);
                break;
            }
            case 2: {
                holder.newsTitle.setText(news.title);
                break;
            }
        }
    }

    @Override
    public MyViewHolder onCreateVH(ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 0: {
                itemView = inflate.inflate(R.layout.news_row1, parent, false);
                break;
            }
            case 1: {
                itemView = inflate.inflate(R.layout.news_row2, parent, false);
                break;
            }
            case 2: {
                itemView = inflate.inflate(R.layout.news_row3, parent, false);
                break;
            }
        }
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        String seat = mList.get(position).seat;
        if (seat.equals("right")) {
            return 2;
        } else if (seat.equals("center")) {
            return 1;
        }
        return 0;
    }

    public class MyViewHolder extends ZViewHolder {
        TextView newsTitle;
        ImageView newsImage1;
        ImageView newsImage2;
        ImageView newsImage3;
        TextView newsIssuedFromTv;
        TextView newsCommentsNoTv;
        TextView newsPublishTime;

        public MyViewHolder(View v) {
            super(v);
            newsTitle = (TextView) v.findViewById(R.id.news_title);
            newsImage1 = v.findViewById(R.id.news_image_1);
            newsImage2 = v.findViewById(R.id.news_image_2);
            newsImage3 = v.findViewById(R.id.news_image_3);
            newsIssuedFromTv = v.findViewById(R.id.news_sources);
            newsCommentsNoTv = v.findViewById(R.id.news_comments_no);
            newsPublishTime = v.findViewById(R.id.news_publish_time);
        }
    }
}
