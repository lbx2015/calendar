package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.adapter.RelatedQuestionAdapter;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class RaiseQuestionActivity extends AppCompatActivity {
    TextInputEditText textInputEditText;
    TextView nextStep;
    RecyclerView recyclerView;
    RelatedQuestionAdapter mAdapter;
    View divider;

    public void clickCancel(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_question);
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
            }
        });
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    nextStep.setTextColor(ZR.getColor(R.color.color_489dfff));
                    nextStep.setEnabled(true);
                } else {
                    nextStep.setTextColor(ZR.getColor(R.color.color_cccccc));
                    nextStep.setEnabled(false);
                }
                //todo test only
                mAdapter.add("a");
            }
        });
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler_view);
        nextStep = findViewById(R.id.next_step);
        textInputEditText = findViewById(R.id.question_input);
    }

    private void setRecyclerView() {
        divider = findViewById(R.id.divider);
        //set layout manager for the recycler view.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RelatedQuestionAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (mAdapter != null) {
                    if (mAdapter.getItemCount() == 0) {
                        divider.setVisibility(View.GONE);
                    } else {
                        divider.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

}
