package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.riking.calendar.R;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.VocationRecyclerViewAdapter;
import com.riking.calendar.listener.HideShowScrollListener;
import com.riking.calendar.realm.model.Vocation;
import com.riking.calendar.util.ViewFindUtils;
import com.riking.calendar.util.ZR;
import com.riking.calendar.widget.dialog.SearchDialog;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class SecondFragment extends Fragment {
    RecyclerView recyclerView;
    Realm realm;
    ViewPagerActivity a;
    View searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        a = (ViewPagerActivity) getActivity();
        View v = inflater.inflate(R.layout.second_fragment, container, false);
        searchView = v.findViewById(R.id.search);

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchDialog dialog = new SearchDialog(v.getContext());
                dialog.show();

            }
        });

        recyclerView = ViewFindUtils.find(v, R.id.recycler_view);
        recyclerView.addOnScrollListener(new HideShowScrollListener() {
            int marginBottom = (int) ZR.convertDpToPx(a, 48);

            @Override
            public void onHide() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                recyclerView.setLayoutParams(params);
                a.bottomTabs.setVisibility(View.GONE);
                recyclerView.invalidate();
            }

            @Override
            public void onShow() {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                params.setMargins(0, 0, 0, marginBottom);
                recyclerView.setLayoutParams(params);
                recyclerView.invalidate();
                a.bottomTabs.setVisibility(View.VISIBLE);
            }
        });
        realm = Realm.getDefaultInstance();
        // Create the Realm instance
        realm = Realm.getDefaultInstance();
        //insert  to realm
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Add a person
                Vocation v = realm.createObject(Vocation.class, UUID.randomUUID().toString());
                v.date = new Date();
                v.country = "China";
                v.currency = "RMB";
                v.name = "春节";

            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(a.getApplicationContext()));
        List<Vocation> vocations = realm.where(Vocation.class).findAll();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayout.VERTICAL));
        recyclerView.setAdapter(new VocationRecyclerViewAdapter(vocations));
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }
}
