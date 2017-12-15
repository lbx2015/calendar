package com.riking.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.activity.QuestionActivity;
import com.riking.calendar.adapter.base.ZAdater;
import com.riking.calendar.pojo.server.QuestResult;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.base.ZViewHolder;

import java.util.ArrayList;
import java.util.List;


public class RelatedQuestionAdapter extends ZAdater<RelatedQuestionAdapter.MyViewHolder> {
    private Context context;
    public List<QuestResult> mList;

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
        final QuestResult r = mList.get(i);
        h.questionTitle.setText(r.title);
        h.questionSummary.setText(ZR.getNumberString(r.tqFollowNum) + "人关注，" + ZR.getNumberString(r.qanswerNum) + "个回答");

        //go to question activity on click
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(h.itemView.getContext(), QuestionActivity.class);
                i.putExtra(CONST.QUESTION_ID, r.questionId);
                ZGoto.to(i);
            }
        });

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
