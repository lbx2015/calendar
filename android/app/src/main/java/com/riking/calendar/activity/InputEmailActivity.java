package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UpdUserParams;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class InputEmailActivity extends AppCompatActivity implements TextWatcher {
    EditText emaileditText;
    View enterButton;
    boolean buttonAvailable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_email);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //set translucent background for the status bar
        StatusBarUtil.setTranslucent(this);
        emaileditText = (EditText) findViewById(R.id.email_tv);
        //adding text change listener
        emaileditText.addTextChangedListener(InputEmailActivity.this);
        enterButton = findViewById(R.id.enter_button);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!buttonAvailable) {
                    return;
                }

                final Editable number = emaileditText.getText();
                if (number == null) {
                    Toast.makeText(emaileditText.getContext(), "邮箱不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!ZR.isValidEmailFormat(number.toString())) {
                    ZToast.toast("邮箱格式不正确");
                    return;
                }
                if (!ZR.isValidEmailSuffix(number.toString())) {
                    ZToast.toast("邮箱后缀不对");
                    return;
                }

                final UpdUserParams user = new UpdUserParams();
                user.email = number.toString();
                APIClient.modifyUserInfo(user, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        AppUserResp currentUser = ZPreference.getCurrentLoginUser();
                        currentUser.email = number.toString();
                        ZPreference.saveUserInfoAfterLogin(currentUser);
                        ZToast.toast("邮箱添加成功！");
                        UserParams userParams = new UserParams();
                        userParams.email = currentUser.email;
                        APIClient.sendEmailVerifyCode(userParams, new ZCallBackWithFail<ResponseModel<String>>() {
                            @Override
                            public void callBack(ResponseModel<String> response) {
                                Intent i = new Intent(InputEmailActivity.this, InputEmailVerifyCodeActivity.class);
                                i.putExtra(CONST.LOGINING, true);
                                ZGoto.to(i);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        emaileditText.setText(ZPreference.getCurrentLoginUser().email);
        emaileditText.setHint("请输入邮箱");
    }

    public void clickBackNav(View v) {
        onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s != null && !StringUtil.isEmpty(s.toString())) {
            buttonAvailable = true;
            //enable the button
            enterButton.setBackground(getDrawable(R.drawable.new_get_verify_code_back_ground_enabled));
        } else {
            buttonAvailable = false;
            enterButton.setBackground(getDrawable(R.drawable.new_get_verify_code_back_ground));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
