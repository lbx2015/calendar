package com.riking.calendar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.utils.MyLog;
import com.necer.ncalendar.view.IdentifyingCodeView;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.util.ZR;

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
                final String verifyCodes = icv.getTextContent();
                if (verifyCodes.length() == 6) {
                    final ProgressDialog dialog = new ProgressDialog(InputVerifyCodeActivity.this);
                    dialog.setMessage("正在加载中");
                    dialog.setCanceledOnTouchOutside(false);
                    //disable the dialog to be cancelled on back key pressed.
                    dialog.setCancelable(false);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            final AppUser user = new AppUser();
                            user.valiCode = verifyCodes;
                            user.idCode = ZR.getDeviceId();
                            user.idCode = Build.MANUFACTURER + Build.MODEL;
                            APIClient.checkVarificationCode(user, new ZCallBack<ResponseModel<AppUser>>() {
                                @Override
                                public void callBack(ResponseModel<AppUser> response) {
                                    Toast.makeText(getApplicationContext(), response._data.id, Toast.LENGTH_SHORT).show();
                                    MyLog.d("checkVarificationCode");
                                }
                            });
                            startActivity(new Intent(InputVerifyCodeActivity.this, IndustrySelectActivity.class));
                            Toast.makeText(InputVerifyCodeActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
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

    @Override
    protected void onStart() {
        super.onStart();
        time.startTick();
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
        }

        public void startTick() {
            getVerificationCodeButton.setBackground(getDrawable(R.drawable.rounded_login__verify_code_rectangle));
            getVerificationCodeButton.setEnabled(false);
            getVerificationCodeButton.setClickable(false);

            start();
        }

    }
}

