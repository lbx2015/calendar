package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.GetVerificationModel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZR;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class InputCellPhoneNumberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_input_phone_number);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        //set translucent background for the status bar
        StatusBarUtil.setTranslucent(this);
        findViewById(R.id.enter_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText phoneNumber = (EditText) findViewById(R.id.phone_number);
                Editable number = phoneNumber.getText();
                if (number == null) {
                    Toast.makeText(phoneNumber.getContext(), "电话号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phoneDigits = number.toString();
                if (!StringUtil.isMobileNO(phoneDigits)) {
                    Toast.makeText(phoneNumber.getContext(), "电话号码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                final AppUser user = new AppUser();
                user.telephone = phoneDigits;
                user.phoneSeqNum = ZR.getDeviceId();
                APIClient.getVarificationCode(user, new ZCallBack<ResponseModel<AppUser>>() {
                    @Override
                    public void callBack(ResponseModel<AppUser> response) {
                        //Success get the verify code
                        startActivity(new Intent(InputCellPhoneNumberActivity.this, InputVerifyCodeActivity.class));
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
