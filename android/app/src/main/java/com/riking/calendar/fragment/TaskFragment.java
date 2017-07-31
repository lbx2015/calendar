package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.TaskAdapter;
import com.riking.calendar.realm.model.Task;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class TaskFragment extends Fragment {
    public Realm realm;
    RecyclerView recyclerView;
    ViewPagerActivity a;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_fragment, container, false);
        a = (ViewPagerActivity) getActivity();
        setRecyclerView(v);
        return v;
    }

    private void setRecyclerView(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(a.getApplicationContext()));
        realm = Realm.getDefaultInstance();
        //only show the not complete tasks
        List<Task> tasks = realm.where(Task.class).equalTo("isDone", false).findAll();
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(a, LinearLayout.VERTICAL));
        recyclerView.setAdapter(new TaskAdapter(tasks, this));
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                //the data is changed.
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

}
