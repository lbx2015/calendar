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

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.GetVerificationModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class LoginActivity extends AppCompatActivity {
    public EditText phoneNumber;
    public EditText verificationCode;
    public View getVerificationCodeButton;
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
        getVerificationCodeButton = findViewById(R.id.get_verification_code_button);
        getVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable number = phoneNumber.getText();
                if (number == null) {
                    return;
                }
                String Regex = "[^\\d]";
                String PhoneDigits = number.toString().replaceAll(Regex, "");
                if (PhoneDigits.length() != 11) {
                    return;
                }

                user.telephone = PhoneDigits;
                user.phoneSeqNum = uid;
                apiInterface.getVarificationCode(user).enqueue(new Callback<GetVerificationModel>() {
                    @Override
                    public void onResponse(Call<GetVerificationModel> call, Response<GetVerificationModel> response) {
                        GetVerificationModel resource = response.body();
                        Log.d("zzw", "phone number success " + resource);
                    }

                    @Override
                    public void onFailure(Call<GetVerificationModel> call, Throwable t) {
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
                    return;
                }
                String Regex = "[^\\d]";
                String valiCode = code.toString().replaceAll(Regex, "");
                if (valiCode.length() != 6) {
                    return;
                }

                user.valiCode = valiCode;
                apiInterface.checkVarificationCode(user).enqueue(new Callback<GetVerificationModel>() {
                    @Override
                    public void onResponse(Call<GetVerificationModel> call, Response<GetVerificationModel> response) {
                        GetVerificationModel user = response.body();
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor e = pref.edit();
                        e.putBoolean(Const.IS_LOGIN, true);
                        e.putString(Const.USER_ID, user._data.id);
                        e.putString(Const.PHONE_NUMBER, user._data.telephone);
                        e.putString(Const.USER_EMAIL, user._data.email);
                        e.putString(Const.PHONE_SEQ_NUMBER, user._data.phoneSeqNum);
                        e.putString(Const.USER_NAME, user._data.name);
                        e.putString(Const.USER_PASSWORD, user._data.passWord);
                        e.putString(Const.USER_DEPT, user._data.dept);
                        e.putString(Const.USER_COMMENTS, user._data.remark);
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
