package com.riking.calendar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.adapter.RecommendedAdapter;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class PlazaFragment extends Fragment {
    View v;
    RecyclerView recyclerView;
    Activity a;
    RecommendedAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v != null) {
            return v;
        }
        v = inflater.inflate(R.layout.plaza_fragment, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        a = getActivity();
        //horizontal recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(a, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new RecommendedAdapter();
        recyclerView.setAdapter(adapter);
        return v;
    }

}