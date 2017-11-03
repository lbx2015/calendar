package com.riking.calendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;

import java.util.ArrayList;
import java.util.List;


public class IndustryAdapter extends RecyclerView.Adapter<QuestionsViewHolder> {

    public List<String> mList;
    private Context context;

    public IndustryAdapter(Context context) {
        this.context = context;
        mList = new ArrayList<>();
    }

    @Override
    public QuestionsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.industry_item, viewGroup, false);
        return new QuestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuestionsViewHolder h, int i) {
        if (i == 0) {
            h.industryName.setText("金融");
        } else if (i == 2) {
            h.industryName.setText("银行");
        } else if (i == 3) {
            h.industryName.setText("证券");
        } else {
            h.industryName.setText("汽车租赁");
        }
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h.checkImage.getVisibility() == View.VISIBLE) {
                    h.checkImage.setVisibility(View.GONE);
                    h.industryName.setTextColor(h.industryName.getResources().getColor(R.color.black_deep));
                } else {
                    h.checkImage.setVisibility(View.VISIBLE);
                    h.industryName.setTextColor(h.industryName.getResources().getColor(R.color.holidayColor));
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size() + 5;
    }

}
