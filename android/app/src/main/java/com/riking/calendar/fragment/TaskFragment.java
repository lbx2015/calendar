package com.riking.calendar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.riking.calendar.R;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.TaskAdapter;
import com.riking.calendar.helper.SimpleItemTouchHelperCallback;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.view.CustomLinearLayout;

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
    CustomLinearLayout root;
    View checkHistoryButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_fragment, container, false);
        root = (CustomLinearLayout) v.findViewById(R.id.custom_linear_layout);
        checkHistoryButton = v.findViewById(R.id.check_task_history);
        a = (ViewPagerActivity) getActivity();
        setRecyclerView(v);
        root.onDraggingListener = new CustomLinearLayout.OnDraggingListener() {
            @Override
            public void scrollUp() {
                if (checkHistoryButton.getVisibility() == View.VISIBLE) {
                    // Start the animation
//                    checkHistoryButton.animate()
//                            .translationY(0)
//                            .alpha(0.0f).setDuration(500);
                    checkHistoryButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void scrollDown() {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                if ((viewHolder == null) || (viewHolder.itemView.getVisibility() == View.VISIBLE && (checkHistoryButton.getVisibility() == View.GONE || checkHistoryButton.getVisibility() == View.INVISIBLE))) {
//                    root.animate().translationY(checkHistoryButton.getHeight()).setDuration(400);
                    checkHistoryButton.setVisibility(View.VISIBLE);
                }
            }
        };
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
        TaskAdapter adapter = new TaskAdapter(tasks, this);
        recyclerView.setAdapter(adapter);
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                //the data is changed.
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            int last = 0;
            boolean control = true;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.d("zzw", "on scroll state changed: " + newState);

            }

            @Override
            public void onScrolled(RecyclerView view, int x, int y) {
                Log.d("zzw", "on scrolled : x " + x + ": y" + y);
            }
        });
        recyclerView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                Log.d("zzw", "drag event " + event.getAction());
                return false;
            }
        });

        //先实例化Callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        //用Callback构造ItemtouchHelper
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

}
