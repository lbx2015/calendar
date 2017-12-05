package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.activity.NewsDetailActivity;
import com.riking.calendar.pojo.server.News;
import com.riking.calendar.util.ZGoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
    public List<News> mList;

    {
        mList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZGoto.to(NewsDetailActivity.class);
            }
        });
        RequestOptions options = new RequestOptions();
        switch (getItemViewType(position)) {
            case 0: {
                holder.newsTitle.setText("间接持有同业存单该如何填报？");
                Glide.with(holder.newsImage1.getContext()).load(R.drawable.profile2)
                        .apply(options.centerCrop())
                        .into(holder.newsImage1);
                Glide.with(holder.newsImage2.getContext()).load(R.drawable.profile3)
                        .apply(options.centerCrop())
                        .into(holder.newsImage2);
                Glide.with(holder.newsImage3.getContext()).load(R.drawable.banner)
                        .apply(options.centerCrop())
                        .into(holder.newsImage3);
                break;
            }
            case 1: {
                holder.newsTitle.setText("什么是证券投资基金？");
                Glide.with(holder.newsImage1.getContext()).load(R.drawable.profile3)
                        .apply(options.centerCrop())
                        .into(holder.newsImage1);
                break;
            }
            case 2: {
                holder.newsTitle.setText("证券投资基金原来是这么一回事，对投资理财的影响超过你的想象？");
                Glide.with(holder.newsImage1.getContext()).load(R.drawable.banner)
                        .apply(options.centerCrop())
                        .into(holder.newsImage1);

                break;
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void add(News s) {
        mList.add(s);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle;
        ImageView newsImage1;
        ImageView newsImage2;
        ImageView newsImage3;

        public MyViewHolder(View v) {
            super(v);
            newsTitle = (TextView) v.findViewById(R.id.news_title);
            newsImage1 = v.findViewById(R.id.news_image_1);
            newsImage2 = v.findViewById(R.id.news_image_2);
            newsImage3 = v.findViewById(R.id.news_image_3);
        }
    }
}
