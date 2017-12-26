package com.riking.calendar.activity;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.necer.ncalendar.view.IdentifyingCodeView;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.util.ZPreference;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class InputEmailVerifyCodeActivity extends AppCompatActivity {
    public TextView getVerificationCodeButton;
    TextView phoneNumberTV;
    AppUserResp currentUser;
    private IdentifyingCodeView icv;
    private TimeCount time;
    private TextView title;
    private LinearLayout bottomLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        time = new TimeCount(60000, 1000);
        setContentView(R.layout.activity_login_input_phone_verify_code);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        title = findViewById(R.id.title);
        //set title
        title.setText("请输入邮箱验证码");
        currentUser = ZPreference.getCurrentLoginUser();
        //set translucent background for the status bar
        StatusBarUtil.setTranslucent(this);

        phoneNumberTV = findViewById(R.id.cell_phone_nubmer);
        icv = (IdentifyingCodeView) findViewById(R.id.icv);
        bottomLayout = findViewById(R.id.bottom_layout);
        //hide the bottom Layout
        bottomLayout.setVisibility(View.GONE);

        phoneNumberTV.setText(currentUser.email);
        icv.setInputCompleteListener(new IdentifyingCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                final String verifyCodes = icv.getTextContent();
                if (verifyCodes.length() == 6) {
                    final ProgressDialog dialog = new ProgressDialog(InputEmailVerifyCodeActivity.this);
                    dialog.setMessage("正在加载中");
                    dialog.setCanceledOnTouchOutside(false);
                    //disable the dialog to be cancelled on back key pressed.
                    dialog.setCancelable(false);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.show();

                    final UserParams user = new UserParams();
                    user.verifyCode = verifyCodes;
                    user.email = currentUser.email;
                    APIClient.emailIdentify(user, new ZCallBackWithFail<ResponseModel<String>>() {
                        @Override
                        public void callBack(ResponseModel<String> response) throws ClassNotFoundException {
                            dialog.dismiss();
                            if (failed) {
                                icv.clearAllText();
                                Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            currentUser.isIdentify = 1;
                            ZPreference.saveUserInfoAfterLogin(currentUser);

                            Intent i = new Intent();
                            i.putExtra(CONST.EMAIL_VALIDATE, 1);
                            setResult(RESULT_OK, i);
                            //kill self in order to return back.
                            finish();
                            Toast.makeText(InputEmailVerifyCodeActivity.this, " 验证成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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

    public void clickBackNav(View v) {
        onBackPressed();
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
            getVerificationCodeButton.setTextColor(getVerificationCodeButton.getResources().getColor(R.color.color_489dfff));
            getVerificationCodeButton.setEnabled(true);
            getVerificationCodeButton.setClickable(true);
        }

        public void startTick() {
            getVerificationCodeButton.setTextColor(getVerificationCodeButton.getResources().getColor(R.color.color_d2d2d2));
            getVerificationCodeButton.setEnabled(false);
            getVerificationCodeButton.setClickable(false);

            start();
        }

    }
}

