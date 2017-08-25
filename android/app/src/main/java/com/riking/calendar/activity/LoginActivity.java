package com.riking.calendar.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.GetVerificationModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.TimeUtil;
import com.riking.calendar.util.image.StringUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class LoginActivity extends AppCompatActivity {
    public EditText phoneNumber;
    public EditText verificationCode;
    public TextView getVerificationCodeButton;
    View loginButton;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    //device id
    String uid;
    AppUser user = new AppUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        uid = tManager.getDeviceId();
        Logger.d("zzw", "device id:" + uid);
        setContentView(R.layout.activity_login);
        phoneNumber = (EditText) findViewById(R.id.phone_nubmer_editor);
        verificationCode = (EditText) findViewById(R.id.verification_code);
        loginButton = findViewById(R.id.login);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getVerificationCodeButton = (TextView) findViewById(R.id.get_verification_code_button);
        getVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                user.telephone = phoneDigits;
                user.phoneSeqNum = uid;
                apiInterface.getVarificationCode(user).enqueue(new Callback<GetVerificationModel>() {
                    @Override
                    public void onResponse(Call<GetVerificationModel> call, Response<GetVerificationModel> response) {
                        GetVerificationModel resource = response.body();
                        Log.d("zzw", "phone number success " + resource);
                        new TimeUtil(getVerificationCodeButton).RunTimer();
                    }

                    @Override
                    public void onFailure(Call<GetVerificationModel> call, Throwable t) {
                        new TimeUtil(getVerificationCodeButton).RunTimer();
                        Log.d("zzw", "failed ");
                    }
                });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable code = verificationCode.getText();
                if (code == null) {
                    Toast.makeText(LoginActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String Regex = "[^\\d]";
                String valiCode = code.toString().replaceAll(Regex, "");
                if (valiCode.length() != 6) {
                    Toast.makeText(LoginActivity.this, "验证码格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                user.valiCode = valiCode;
                apiInterface.checkVarificationCode(user).enqueue(new Callback<GetVerificationModel>() {
                    @Override
                    public void onResponse(Call<GetVerificationModel> call, Response<GetVerificationModel> response) {
                        GetVerificationModel user = response.body();
                        if (user == null || user._data == null) {
                            Toast.makeText(LoginActivity.this, getString(R.string.login_failed), Toast.LENGTH_SHORT);
                            return;
                        }
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor e = pref.edit();
                        e.putBoolean(Const.IS_LOGIN, true);
                        e.putString(Const.USER_ID, user._data.id);
                        e.putString(Const.USER_IMAGE_URL, user._data.photoUrl);
                        e.putString(Const.PHONE_NUMBER, user._data.telephone);
                        e.putString(Const.USER_EMAIL, user._data.email);
                        e.putString(Const.PHONE_SEQ_NUMBER, user._data.phoneSeqNum);
                        e.putString(Const.USER_NAME, user._data.name);
                        e.putString(Const.USER_PASSWORD, user._data.passWord);
                        e.putString(Const.USER_DEPT, user._data.dept);
                        e.putInt(Const.USER_SEX, user._data.sex);
                        e.putString(Const.USER_COMMENTS, user._data.remark);
                        e.putString(Const.USER_COMMENTS, user._data.remark);
                        e.putString(Const.WHOLE_DAY_EVENT_HOUR, user._data.allDayReminderTime.substring(0, 2));
                        e.putString(Const.WHOLE_DAY_EVENT_MINUTE, user._data.allDayReminderTime.substring(2));
                        e.commit();
                        onBackPressed();
                        startActivity(new Intent(LoginActivity.this, UserInfoActivity.class));
                        Logger.d("zzw", "login succes : " + user);
                    }

                    @Override
                    public void onFailure(Call<GetVerificationModel> call, Throwable t) {

                    }
                });
            }
        });
    }
}
