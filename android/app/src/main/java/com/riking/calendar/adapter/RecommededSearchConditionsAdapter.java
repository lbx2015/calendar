package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.riking.calendar.R;
import com.riking.calendar.interfeet.PerformSearch;
import com.riking.calendar.pojo.server.HotSearch;
import com.riking.calendar.util.ZR;
import com.riking.calendar.viewholder.OneTextViewHolder;

import java.util.List;


public class RecommededSearchConditionsAdapter extends RecyclerView.Adapter<OneTextViewHolder> {

    public List<HotSearch> mList;
    private PerformSearch searchListener;

    public RecommededSearchConditionsAdapter(PerformSearch searchListener) {
        this.searchListener = searchListener;
    }

    @Override
    public OneTextViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.recommended_search_condition, viewGroup, false);
        return new OneTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OneTextViewHolder h, final int i) {
        final HotSearch m = mList.get(i);
        h.textView.setText(m.title);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                searchListener.performSearchByLocalHistory(m.title);
            }
        });

        //first column
        if (i % 2 == 0) {

        }
        //second column
        else {
            if (h.divider != null) {
                h.divider.setVisibility(View.GONE);
            }

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) h.textView.getLayoutParams();
            layoutParams.leftMargin = (int) ZR.convertDpToPx(15);
        }

        int size = getItemCount();
        if (size % 2 == 0) {
            if (i >= size - 2) {
                h.horizontalDivider.setVisibility(View.GONE);
            }
        } else {
            if (i == size - 1) {
                h.horizontalDivider.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        int size = mList == null ? 0 : mList.size();
        //empty notice
        searchListener.localSearchConditionIsEmpty(size == 0);
        return size;
    }

    public void setData(List<HotSearch> list) {
        mList = list;
        notifyDataSetChanged();
    }
}
