package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.NewsDetailActivity;
import com.riking.calendar.pojo.server.NewsResult;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.DateUtil;
import com.riking.calendar.util.ZGoto;

import java.util.ArrayList;
import java.util.List;


public class SearchNewsAdapter extends RecyclerView.Adapter<SearchNewsAdapter.MyViewHolder> {
    private Context context;
    private List<NewsResult> mList;

    public SearchNewsAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public SearchNewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.search_news_item, viewGroup, false);
        return new SearchNewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SearchNewsAdapter.MyViewHolder h, int i) {
        final NewsResult news = mList.get(i);
        h.newsTitleTv.setText(news.title);
        h.newsUpdateTimeTv.setText(DateUtil.date2String(news.createdTime, CONST.yyyy_mm_dd_hh_mm));

        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, NewsDetailActivity.class);
                i.putExtra(CONST.NEWS_ID, news.newsId);
                MyLog.d("click newsId " + news.newsId);
                ZGoto.to(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<NewsResult> data) {
        this.mList = data;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView newsTitleTv;
        TextView newsUpdateTimeTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            newsTitleTv = itemView.findViewById(R.id.news_title);
            newsUpdateTimeTv = itemView.findViewById(R.id.news_update_title);
        }
    }
}
