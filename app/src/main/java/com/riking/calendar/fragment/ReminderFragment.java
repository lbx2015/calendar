package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.riking.calendar.R;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.ReminderRecyclerViewAdapter;
import com.riking.calendar.realm.model.Reminder;

import java.util.List;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class ReminderFragment extends Fragment {
    RecyclerView recyclerView;
    Realm realm;
    ViewPagerActivity a;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.reminder_fragment, container, false);
        a = (ViewPagerActivity) getActivity();
        setRecyclerView(v);
        return v;
    }

    private void setRecyclerView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(a.getApplicationContext()));
        realm = Realm.getDefaultInstance();
        List<Reminder> reminders = realm.where(Reminder.class).findAll();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayout.VERTICAL));
        recyclerView.setAdapter(new ReminderRecyclerViewAdapter(reminders));
    }
}
