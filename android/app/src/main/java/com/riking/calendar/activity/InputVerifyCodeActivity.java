package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.necer.ncalendar.view.IdentifyingCodeView;
import com.riking.calendar.R;
import com.riking.calendar.util.StatusBarUtil;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class InputVerifyCodeActivity extends AppCompatActivity {
    private IdentifyingCodeView icv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_input_phone_verify_code);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //set translucent background for the status bar
        StatusBarUtil.setTranslucent(this);
        findViewById(R.id.enter_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InputVerifyCodeActivity.this, LoginActivity.class));
            }
        });

        icv = (IdentifyingCodeView) findViewById(R.id.icv);

        icv.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
//                startActivity(new Intent(InputVerifyCodeActivity.this, LoginActivity.class));
            }

            @Override
            public void deleteContent() {
                Log.i("icv_delete", icv.getTextContent());
            }
        });
    }

    public void onClick(View view) {
        icv.clearAllText();
    }
}
