package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.necer.ncalendar.view.IdentifyingCodeView;
import com.riking.calendar.R;
import com.riking.calendar.util.StatusBarUtil;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class InputVerifyCodeActivity extends AppCompatActivity {
    public TextView getVerificationCodeButton;
    private IdentifyingCodeView icv;
    private TimeCount time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = new TimeCount(60000, 1000);

        setContentView(R.layout.activity_login_input_phone_verify_code);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //set translucent background for the status bar
        StatusBarUtil.setTranslucent(this);

        icv = (IdentifyingCodeView) findViewById(R.id.icv);

        icv.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (icv.getTextContent().length() == 6) {
                    time.startTick();
                }
//                startActivity(new Intent(InputVerifyCodeActivity.this, LoginActivity.class));
            }

            @Override
            public void deleteContent() {
                Log.i("icv_delete", icv.getTextContent());
            }
        });

        //resent button
        getVerificationCodeButton = (TextView) findViewById(R.id.get_verification_code_button);
        getVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public void onClick(View view) {
        icv.clearAllText();
    }

    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            getVerificationCodeButton.setText(+millisUntilFinished / 1000 + "s后重新发送");
        }

        @Override
        public void onFinish() {
            getVerificationCodeButton.setText("重新获取");
            getVerificationCodeButton.setBackground(getDrawable(R.drawable.rounded_login__verify_code_color_rectangle));
            getVerificationCodeButton.setEnabled(true);
            getVerificationCodeButton.setClickable(true);
            //user can input the verify codes one more time
            icv.setEnabled(true);
            icv.setFocusable(true);
        }

        public void startTick() {
            getVerificationCodeButton.setBackground(getDrawable(R.drawable.rounded_login__verify_code_rectangle));
            getVerificationCodeButton.setEnabled(false);
            getVerificationCodeButton.setClickable(false);
            //disable the editing fucntion of verify code.
            icv.setEnabled(false);
            icv.setFocusable(false);

            start();
        }

    }
}

