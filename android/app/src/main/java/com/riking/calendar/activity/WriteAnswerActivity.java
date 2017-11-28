package com.riking.calendar.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.util.ZR;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class WriteAnswerActivity extends AppCompatActivity {
    TextInputEditText textInputEditText;
    TextView publishButton;

    public void clickCancel(View view) {
        onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_answer);
        init();
    }

    void init() {
        initViews();
        initEvents();
    }

    private void initEvents() {
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WriteAnswerActivity.this, "成功", Toast.LENGTH_SHORT).show();
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
                    publishButton.setTextColor(ZR.getColor(R.color.color_489dfff));
                    publishButton.setEnabled(true);
                } else {
                    publishButton.setTextColor(ZR.getColor(R.color.color_cccccc));
                    publishButton.setEnabled(false);
                }
            }
        });
    }

    private void initViews() {
        publishButton = findViewById(R.id.publish_button);
        textInputEditText = findViewById(R.id.answer_input);
    }

}
