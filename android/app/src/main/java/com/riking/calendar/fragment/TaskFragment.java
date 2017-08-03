package com.riking.calendar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.activity.TaskHistoryActivity;
import com.riking.calendar.activity.ViewPagerActivity;
import com.riking.calendar.adapter.TaskAdapter;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.view.CustomLinearLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by zw.zhang on 2017/7/17.
 */

public class TaskFragment extends Fragment {
    public Realm realm;
    protected SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    ViewPagerActivity a;
    CustomLinearLayout root;
    View checkHistoryButton;
    TaskAdapter adapter;
    View quickAddButton;
    View quickAddFrameLayout;
    EditText quickAddEditor;
    View quickAddConfirmButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.task_fragment, container, false);
        root = (CustomLinearLayout) v.findViewById(R.id.custom_linear_layout);
        quickAddButton = v.findViewById(R.id.quick_add_button);
        quickAddFrameLayout = v.findViewById(R.id.quick_add_frame_layout);
        quickAddEditor = (EditText) v.findViewById(R.id.quick_add_editer);
        quickAddConfirmButton = v.findViewById(R.id.quick_add_confirm_button);

        quickAddEditor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                quickAddConfirmButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        quickAddConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        Task task = realm.createObject(Task.class, simpleDateFormat.format(new Date()));
                        task.title = quickAddEditor.getText().toString();
                    }
                });
                quickAddEditor.setText(null);
                quickAddConfirmButton.setVisibility(View.GONE);
                quickAddFrameLayout.setVisibility(View.GONE);
                quickAddButton.setVisibility(View.VISIBLE);
            }
        });

        quickAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAddButton.setVisibility(View.GONE);
                quickAddFrameLayout.setVisibility(View.VISIBLE);
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(root.getContext(), "Refresh success", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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

        checkHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open the task history page
                startActivity(new Intent(getContext(), TaskHistoryActivity.class));
            }
        });
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
        adapter = new TaskAdapter(tasks, this);
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

//        //先实例化Callback
//        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
//        //用Callback构造ItemtouchHelper
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        //调用ItemTouchHelper的attachToRecyclerView方法建立联系
//        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
    }

}
