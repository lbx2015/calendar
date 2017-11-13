package com.riking.calendar.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.necer.ncalendar.view.IdentifyingCodeView;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.synch.SynResult;
import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.util.ZR;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by zw.zhang on 2017/8/14.
 */

public class InputVerifyCodeActivity extends AppCompatActivity {
    public TextView getVerificationCodeButton;
    TextView phoneNumberTV;
    String phoneNumber;
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
        phoneNumber = getIntent().getExtras().getString(CONST.PHONE_NUMBER);

        phoneNumberTV = findViewById(R.id.cell_phone_nubmer);
        icv = (IdentifyingCodeView) findViewById(R.id.icv);

        phoneNumberTV.setText(phoneNumber);
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

                    final AppUser user = new AppUser();
                    user.valiCode = verifyCodes;
                    user.phoneSeqNum = ZR.getDeviceId();
                    user.telephone = phoneNumber;
                    APIClient.checkVarificationCode(user, new ZCallBackWithFail<ResponseModel<AppUser>>() {
                        @Override
                        public void callBack(ResponseModel<AppUser> response) {
                            dialog.dismiss();
                            if (failed) {
                                icv.clearAllText();
                                Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                                return;
                            }
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
                            APIClient.apiInterface.synchronousAll(jsonObject).enqueue(new ZCallBack<ResponseModel<SynResult>>() {
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

                            startActivity(new Intent(InputVerifyCodeActivity.this, IndustrySelectActivity.class));
                            Toast.makeText(InputVerifyCodeActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        }
                    });
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
            getVerificationCodeButton.setTextColor(getVerificationCodeButton.getResources().getColor(R.color.button_enabled));
            getVerificationCodeButton.setEnabled(true);
            getVerificationCodeButton.setClickable(true);
        }

        public void startTick() {
            getVerificationCodeButton.setTextColor(getVerificationCodeButton.getResources().getColor(R.color.grey_border));
            getVerificationCodeButton.setEnabled(false);
            getVerificationCodeButton.setClickable(false);

            start();
        }

    }
}

