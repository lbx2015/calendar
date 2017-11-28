package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class RaiseQuestionActivity extends AppCompatActivity {
    TextInputEditText textInputEditText;
    TextView nextStep;

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
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZGoto.to(RaiseQuestionActivity.this, RelatedQuestionActivity.class);
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
            }
        });
    }

    private void initViews() {
        nextStep = findViewById(R.id.next_step);
        textInputEditText = findViewById(R.id.question_input);
    }

}
