package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.adapter.RelatedQuestionAdapter;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class RelatedQuestionActivity extends AppCompatActivity {
    TextView nextStep;
    RecyclerView recyclerView;
    RelatedQuestionAdapter mAdapter;

    public void clickCancel(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_question);
        init();
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initEvents() {
        setRecyclerView();
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RelatedQuestionActivity.this, "成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        nextStep = findViewById(R.id.next_step);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void setRecyclerView() {
        //set layout manager for the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RelatedQuestionAdapter(this);
        mAdapter.add("dd");
        mAdapter.add("dd");
        mAdapter.add("dd");
        mAdapter.add("ddlllljjijjlkkj");
        recyclerView.setAdapter(mAdapter);
    }

}
