package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.MyViewHolder> {
    private Context a;
    private List<String> mList;

    public CommentListAdapter(Context context) {
        this.a = context;
        mList = new ArrayList<>();
    }

    @Override
    public CommentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.comment_list_item, viewGroup, false);
        return new CommentListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentListAdapter.MyViewHolder h, int i) {
        RequestOptions options = new RequestOptions();
        Glide.with(h.authorImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.authorImage);
        setRecyclerView(h.recyclerView);

    }

    private void setRecyclerView(final RecyclerView recyclerView) {
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        ReplyListAdapter mAdapter = new ReplyListAdapter();
        mAdapter.add("dd");
        mAdapter.add("d");
        recyclerView.setAdapter(mAdapter);
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

    class MyViewHolder extends RecyclerView.ViewHolder {
        public CircleImageView authorImage;

        public RecyclerView recyclerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            authorImage = itemView.findViewById(R.id.answer_author_icon);
            recyclerView = itemView.findViewById(R.id.recycler_view);
        }
    }
}
