package com.riking.calendar.activity;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.google.gson.JsonObject;
import com.necer.ncalendar.view.IdentifyingCodeView;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.pojo.synch.LoginParams;
import com.riking.calendar.pojo.synch.SynResult;
import com.riking.calendar.realm.model.Task;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.StatusBarUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;

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

                    final LoginParams user = new LoginParams();
                    user.verifyCode = verifyCodes;
                    user.phoneDeviceId = ZR.getRegistrationId();
                    user.phone = phoneNumber;
                    APIClient.checkVarificationCode(user, new ZCallBackWithFail<ResponseModel<AppUserResp>>() {
                        @Override
                        public void callBack(ResponseModel<AppUserResp> response) throws ClassNotFoundException {
                            dialog.dismiss();
                            if (failed) {
                                icv.clearAllText();
                                Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            AppUserResp u = response._data;

                            //save user info in preference
                            ZPreference.saveUserInfoAfterLogin(u);

                            //change realm database with user userId
                            RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                                    .deleteRealmIfMigrationNeeded();
                            builder.name(u.userId);
                            Realm.setDefaultConfiguration(builder.build());
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("userId", u.userId);
                            //get user's reminders and tasks
                            APIClient.apiInterface.synchronousAll(jsonObject).enqueue(new ZCallBack<ResponseModel<SynResult>>() {
                                @Override
                                public void callBack(final ResponseModel<SynResult> response) {
                                    final Realm realm = Realm.getDefaultInstance();
                                    realm.executeTransactionAsync(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
//                                            List<ReminderModel> reminders = response._data.remind;
                                            List<TaskModel> tasks = response._data.todo;
//                                            if (reminders != null) {
//                                                for (ReminderModel m : reminders) {
//                                                    Reminder r = new Reminder(m);
//                                                    realm.copyToRealmOrUpdate(r);
//                                                }
//                                            }
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
                            MANService manService = MANServiceProvider.getService();
// 用户登录埋点   // 用户登录埋点
                            manService.getMANAnalytics().updateUserAccount(u.userName, u.userId);
                            if (u.isIdentify == 0) {
                                ZGoto.to(InputEmailActivity.class);
                            } else if (ZR.jumpClass != null) {
                                //delete the the jump class name
                                Class<Activity> c = (Class<Activity>) Class.forName(ZR.jumpClass);
                                ZGoto.to(c);
                                ZR.jumpClass = null;
                            }
                            //sent broadcast when not login check by on click
                            else if (ZPreference.pref.getBoolean(CONST.CHECK_NOT_LOGIN_ON_CLICK, false)) {
                                ZPreference.remove(CONST.CHECK_NOT_LOGIN_ON_CLICK);
                                Intent intent = new Intent("android.intent.action.MY_BROADCAST");
                                intent.putExtra("msg", "hello receiver.");
                                sendBroadcast(intent);
                            } else if (u.isGuide == (0)) {
                                ZGoto.to(IndustrySelectActivity.class);
                            }

                            //kill self in order to return back.
                            finish();
//                            Toast.makeText(InputVerifyCodeActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
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
                final LoginParams user = new LoginParams();
                user.phone = phoneNumber;
                final ProgressDialog dialog = new ProgressDialog(InputVerifyCodeActivity.this);
                dialog.setMessage("正在加载中");
                dialog.show();
                APIClient.getVarificationCode(user, new ZCallBackWithFail<ResponseModel<AppUser>>() {
                    @Override
                    public void callBack(ResponseModel<AppUser> u) {
                        dialog.dismiss();
                        if (failed) {
                            ZToast.toast("获取失败");
                        } else {
                            ZToast.toast("发送成功");
                            finish();
                        }
                    }
                });
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
            getVerificationCodeButton.setText("重新发送验证码");
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

