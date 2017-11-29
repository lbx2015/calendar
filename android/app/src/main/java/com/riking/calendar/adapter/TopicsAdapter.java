package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.activity.AddTopicActivity;
import com.riking.calendar.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.MyViewHolder> {
    private Context context;
    private List<String> mList;

    public TopicsAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public TopicsAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.topic_item, viewGroup, false);
        return new TopicsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TopicsAdapter.MyViewHolder h, int i) {
        h.userName.setText("周润发" + i);
        h.userName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_icon_grade_v3, 0);
        RequestOptions options = new RequestOptions();
        Glide.with(h.userImage.getContext()).load(R.drawable.img_user_head)
                .apply(options.fitCenter())
                .into(h.userImage);

        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof AddTopicActivity) {
                    AddTopicActivity a = (AddTopicActivity) context;
                    a.clickAddTopic(h.userName.getText().toString());
                }
            }
        });
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


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.answer_summary)
        public TextView answerSummary;
        @BindView(R.id.user_name)
        public TextView userName;
        @BindView(R.id.user_icon)
        public CircleImageView userImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setTag(this);
        }
    }
}
