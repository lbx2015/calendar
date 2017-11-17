package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.TopicActivity;
import com.riking.calendar.viewholder.OneTextViewHolder;
import com.riking.calendar.viewholder.RecommendedViewHolder;

import java.util.ArrayList;
import java.util.List;


public class QuestionsAdapter extends RecyclerView.Adapter {

    public static final int REMMEND_TYPE = 2;
    public List<String> mList;
    private Context context;

    public QuestionsAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == REMMEND_TYPE) {
            RecyclerView recyclerView = new RecyclerView(viewGroup.getContext());
            recyclerView.setId(R.id.recycler_view);
            return new RecommendedViewHolder(recyclerView);
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.question_item, viewGroup, false);
            return new OneTextViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 5) {
            return REMMEND_TYPE;
        }
        return super.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder cellHolder, int i) {
        if (getItemViewType(i) == REMMEND_TYPE) {
            MyLog.d("onBindViewHolderr at " + i + " and the view type is " + getItemViewType(i));
            RecommendedViewHolder h = (RecommendedViewHolder) cellHolder;
            h.recyclerView.setLayoutManager(new LinearLayoutManager(h.recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            h.recyclerView.setAdapter(new RecommendedAdapter());

        } else {
            OneTextViewHolder h = (OneTextViewHolder) cellHolder;
            h.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, TopicActivity.class));
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        return mList.size() + 5;
    }

    public void add(String s) {
        mList.add(s);
        notifyItemInserted(mList.size() - 1);
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }
}
