package com.riking.calendar.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.GetVerificationModel;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.synch.SynResult;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StringUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
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
    public TextView userContractLinkButton;
    Button loginButton;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    //device id
    String uid;
    private TimeCount time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final AppUser user = new AppUser();
        TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            uid = tManager.getDeviceId();
        }
        time = new TimeCount(60000, 1000);
        Logger.d("zzw", "device id:" + uid);
        setContentView(R.layout.activity_login);
        phoneNumber = (EditText) findViewById(R.id.phone_nubmer_editor);
        verificationCode = (EditText) findViewById(R.id.verification_code);
        userContractLinkButton = (TextView) findViewById(R.id.user_contract);
        loginButton = (Button) findViewById(R.id.login);
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
                        time.startTick();
                    }

                    @Override
                    public void onFailure(Call<GetVerificationModel> call, Throwable t) {
                        time.startTick();
                        Log.d("zzw", "failed ");
                    }
                });
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
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
                apiInterface.checkVarificationCode(user).enqueue(new ZCallBack<ResponseModel<AppUser>>() {
                    @Override
                    public void callBack(ResponseModel<AppUser> response) {
                        AppUser u = response._data;
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(CONST.PREFERENCE_FILE_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor e = pref.edit();
                        e.putBoolean(CONST.IS_LOGIN, true);
                        e.putString(CONST.USER_ID, u.id);
                        e.putString(CONST.USER_IMAGE_URL, u.photoUrl);
                        e.putString(CONST.PHONE_NUMBER, u.telephone);
                        e.putString(CONST.USER_EMAIL, u.email);
                        e.putString(CONST.PHONE_SEQ_NUMBER, u.phoneSeqNum);
                        e.putString(CONST.USER_NAME, u.name);
                        e.putString(CONST.USER_PASSWORD, u.passWord);
                        e.putString(CONST.USER_DEPT, u.dept);
                        e.putInt(CONST.USER_SEX, u.sex);
                        e.putString(CONST.USER_COMMENTS, u.remark);
                        e.putString(CONST.USER_COMMENTS, u.remark);
                        if (u.allDayReminderTime != null) {
                            e.putString(CONST.WHOLE_DAY_EVENT_HOUR, u.allDayReminderTime.substring(0, 2));
                            e.putString(CONST.WHOLE_DAY_EVENT_MINUTE, u.allDayReminderTime.substring(2));
                        }
                        e.commit();

                        //change realm database with user id
                        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                                .deleteRealmIfMigrationNeeded();
                        builder.name(u.id);
                        Realm.setDefaultConfiguration(builder.build());
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("id", u.id);
                        //get user's reminders and tasks
                        apiInterface.synchronousAll(jsonObject).enqueue(new ZCallBack<ResponseModel<SynResult>>() {
                            @Override
                            public void callBack(final ResponseModel<SynResult> response) {
                                final Realm realm = Realm.getDefaultInstance();
                                realm.executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        List<ReminderModel> reminders = response._data.remind;
                                        List<TaskModel> tasks = response._data.todo;
                                        if (reminders != null) {
                                            for (ReminderModel m : reminders) {
                                                Reminder r = new Reminder(m);
                                                realm.copyToRealmOrUpdate(r);
                                            }
                                        }
                                        if (tasks != null) {
                                            for (TaskModel m : tasks) {
                                                Task t = new Task(m);
                                                realm.copyToRealmOrUpdate(t);
                                            }
                                        }
                                    }
                                });
                            }
                        });
                        onBackPressed();
                        startActivity(new Intent(LoginActivity.this, UserInfoActivity.class));
                        Logger.d("zzw", "login succes : " + user);
                    }
                });
            }
        });

        //set the message verify code after 60seconds
        addVerifyCodeInputWatch();

        userContractLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiInterface.getAgreementHtml(null).enqueue(new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        Intent i = new Intent(LoginActivity.this, WebviewActivity.class);
                        i.putExtra(CONST.WEB_URL, response._data);
                        startActivity(i);
                    }
                });

            }
        });
    }

    private void addVerifyCodeInputWatch() {
        verificationCode.addTextChangedListener(new TextWatcher() {
            //    Tips:
            //    1. onTextChanged和beforTextChanged传入的参数s其实是当前EditText的文字内容，而不是当前输入的内容
            //    2. 如果在任意一个方法中调用了设置当前EditText文本的方法，setText()，实际都触发了一遍这3个函数，
            //       所以要有判断条件，在if体内去setText，而且就需要手动设置光标的位置，不然每次光标都会到最开始的位置
            //    3. onTextChanged中，before=0： 增加；before=1： 点击删除按键

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    Logger.d("zzw", "after changed " + s);
                    if (s.toString().trim().matches("\\d{6}")) {
                        Logger.d("zzw", "pass check changed " + s);
                        loginButton.setEnabled(true);
                        loginButton.setBackground(getDrawable(R.drawable.rounded_login_color_rectangle));
                    } else {
                        loginButton.setEnabled(false);
                        loginButton.setBackground(getDrawable(R.drawable.rounded_login_rectangle));
                    }
                }
            }
        });

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
