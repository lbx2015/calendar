package com.riking.calendar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.SearchReportActivity;
import com.riking.calendar.realm.model.SearchConditions;
import com.riking.calendar.viewholder.OneTextViewHolder;

import java.util.List;

import io.realm.RealmResults;


public class LocalSearchConditionAdapter extends RecyclerView.Adapter<OneTextViewHolder> {

    public List<SearchConditions> mList;
    private SearchReportActivity activity;

    public LocalSearchConditionAdapter(SearchReportActivity activity, RealmResults<SearchConditions> realmResults) {
        this.activity = activity;
        mList = realmResults;
    }

    @Override
    public OneTextViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.local_search_condition, viewGroup, false);
        return new OneTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OneTextViewHolder h, final int i) {
        final SearchConditions m = mList.get(i);
        h.textView.setText(m.name);
        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                activity.reportSearchCondition = m.name;
                activity.performSearch();
                activity.saveToRealm();
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = mList.size();
        //empty notice
        if (size == 0) {
            activity.localSearchConditionIsEmpty();
        }
        return size;
    }

    public void setData(List<SearchConditions> list) {
        mList = list;
        notifyDataSetChanged();
    }
}