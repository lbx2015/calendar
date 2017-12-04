package com.riking.calendar.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.FileUtil;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZDB;
import com.riking.calendar.widget.dialog.TimeClockPickerDialog;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    //timeView
    String hour, minute;

    TextView wholeDayEventTime;
    TextView cacheSizeTextview;
    SharedPreferences preferences;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    boolean doubleBackToClearMemory;
    TextView bindedPhone;
    private TimeClockPickerDialog pickerDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(CONST.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_setting);
        cacheSizeTextview = (TextView) findViewById(R.id.cache_size);
        bindedPhone = (TextView) findViewById(R.id.binded);
        //set the image cache file size
        long imageSize = FileUtil.getFileSize(new File(Environment.getExternalStorageDirectory(), CONST.IMAGE_PATH));
        if (imageSize > 0) {
            cacheSizeTextview.setText(FileUtil.formatFileSize(imageSize));
        } else {
            cacheSizeTextview.setText(getString(R.string.no_need_to_clear));
        }

        if (ZPreference.pref.getBoolean(CONST.IS_LOGIN, false)) {
            findViewById(R.id.login_out_card_view).setVisibility(View.VISIBLE);
            bindedPhone.setText(ZPreference.pref.getString(CONST.PHONE_NUMBER, ""));
        } else {
            bindedPhone.setText(getString(R.string.not_binded));
        }

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.login_out_button).setOnClickListener(this);
        findViewById(R.id.whole_day_event_time_relative_layout).setOnClickListener(this);
        findViewById(R.id.clear_cache_relatvie_layout).setOnClickListener(this);
        findViewById(R.id.bind_phone_relative_layout).setOnClickListener(this);

        wholeDayEventTime = (TextView) findViewById(R.id.event_time);
        if (preferences.getString(CONST.WHOLE_DAY_EVENT_MINUTE, null) != null) {
            wholeDayEventTime.setText(preferences.getString(CONST.WHOLE_DAY_EVENT_HOUR, "") + ":" + preferences.getString(CONST.WHOLE_DAY_EVENT_MINUTE, ""));
        }

        pickerDialog = new TimeClockPickerDialog(this);
        pickerDialog.btnSubmit.setOnClickListener(this);
        pickerDialog.btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
            case R.id.login_out_button: {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                //cancel the alarms
                ZDB.Instance.cancelAlarmsWhenLoginOut();
                //change realm database
                RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded();
                builder.name(CONST.DEFAUT_REALM_DATABASE_NAME);
                Realm.setDefaultConfiguration(builder.build());

//                Intent mStartActivity = new Intent(this, ViewPagerActivity.class);
//                int mPendingIntentId = 123456;
//                PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//                AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
//                System.exit(0);
                finish();
                break;
            }
            case R.id.whole_day_event_time_relative_layout: {
                pickerDialog.show();
                break;
            }

            case R.id.btnSubmit: {
                hour = pickerDialog.wheelTimePicker.hour;
                minute = pickerDialog.wheelTimePicker.minute;

                pickerDialog.dismiss();
                AppUser user = new AppUser();
                user.userId = preferences.getString(CONST.USER_ID, null);
                user.allDayReminderTime = hour + minute;
                apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(CONST.WHOLE_DAY_EVENT_HOUR, hour);
                        editor.putString(CONST.WHOLE_DAY_EVENT_MINUTE, minute);
                        wholeDayEventTime.setText(hour + ":" + minute);
                        editor.commit();
                    }
                });
                break;
            }
            case R.id.btnCancel: {
                pickerDialog.dismiss();
                break;
            }

            case R.id.clear_cache_relatvie_layout: {
                if (doubleBackToClearMemory) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            File imageDirectory = new File(Environment.getExternalStorageDirectory(), CONST.IMAGE_PATH);
                            for (File f : imageDirectory.listFiles()) {
                                f.delete();
                            }
                            SettingActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cacheSizeTextview.setText(cacheSizeTextview.getContext().getString(R.string.no_need_to_clear));
                                    Toast.makeText(cacheSizeTextview.getContext(), cacheSizeTextview.getResources().getString(R.string.cleared), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                    return;
                }

                this.doubleBackToClearMemory = true;
                Toast.makeText(this, getString(R.string.click_again_clear_memory), Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToClearMemory = false;
                    }
                }, 2000);

                break;
            }
            case R.id.bind_phone_relative_layout: {
                String phoneNumber = preferences.getString(CONST.PHONE_NUMBER, "");
                if (!phoneNumber.equals("")) {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage(phoneNumber)
                            .setTitle(R.string.phone_number);

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    break;
                }
            }
        }
    }
}
