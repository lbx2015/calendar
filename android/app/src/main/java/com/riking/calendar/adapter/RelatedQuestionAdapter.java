package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;

import java.util.ArrayList;
import java.util.List;


public class RelatedQuestionAdapter extends RecyclerView.Adapter<RelatedQuestionAdapter.MyViewHolder> {
    private Context context;
    private List<String> mList;

    public RelatedQuestionAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public RelatedQuestionAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.related_question_item, viewGroup, false);
        return new RelatedQuestionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RelatedQuestionAdapter.MyViewHolder h, int i) {
        if (i == 0) {
            h.questionSummary.setVisibility(View.GONE);
        } else if (i == 3) {
            h.questionTitle.setText("请问银监会1104工程和EAST报送系统有什么关系吗？");
        } else {
            h.questionSummary.setVisibility(View.VISIBLE);
        }

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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView questionTitle;
        TextView questionSummary;

        public MyViewHolder(View itemView) {
            super(itemView);
            questionTitle = itemView.findViewById(R.id.question_title);
            questionSummary = itemView.findViewById(R.id.question_summary);
        }
    }
}
