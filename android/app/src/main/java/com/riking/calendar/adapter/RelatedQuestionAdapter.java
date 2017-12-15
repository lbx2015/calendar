package com.riking.calendar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.QuestResult;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.base.ZViewHolder;

import java.util.ArrayList;
import java.util.List;


public class RelatedQuestionAdapter extends ZAdater<RelatedQuestionAdapter.MyViewHolder> {
    private Context context;
    private List<QuestResult> mList;

    public RelatedQuestionAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public RelatedQuestionAdapter.MyViewHolder onCreateVH(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.related_question_item, viewGroup, false);
        return new RelatedQuestionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindVH(final RelatedQuestionAdapter.MyViewHolder h, int i) {
        QuestResult r = mList.get(i);
        h.questionTitle.setText(r.title);
        h.questionSummary.setText(ZR.getNumberString(r.tqFollowNum)+"人关注，"+ZR.getNumberString(r.qanswerNum)+"个回答");

    }


    @Override
    public int getCount() {
        return mList.size();
    }

    public void setData(List<QuestResult> s) {
        mList = s;
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends ZViewHolder {

        TextView questionTitle;
        TextView questionSummary;

        public MyViewHolder(View itemView) {
            super(itemView);
            questionTitle = itemView.findViewById(R.id.question_title);
            questionSummary = itemView.findViewById(R.id.question_summary);
        }
    }
}
