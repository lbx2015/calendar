package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.pojo.server.NewsResult;

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
