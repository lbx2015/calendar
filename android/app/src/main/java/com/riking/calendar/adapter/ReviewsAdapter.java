package com.riking.calendar.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.app.GlideApp;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewViewHolder> {
    Context context;
    ArrayList<String> mList;
    public ReviewsAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();

    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.review_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder h, int position) {
        GlideApp.with(context).load(R.drawable.img_user_head)
                .fitCenter()
                .into(h.userIcon);
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}