package com.riking.calendar.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.GetVerificationModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.widget.dialog.TimeClockPickerDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    //timeView
    String hour, minute;

    TextView wholeDayEventTime;
    SharedPreferences preferences;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    private TimeClockPickerDialog pickerDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_setting);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.login_out_button).setOnClickListener(this);
        findViewById(R.id.whole_day_event_time_relative_layout).setOnClickListener(this);
        wholeDayEventTime = (TextView) findViewById(R.id.event_time);
        if (preferences.getString(Const.WHOLE_DAY_EVENT_MINUTE, null) != null) {
            wholeDayEventTime.setText(preferences.getString(Const.WHOLE_DAY_EVENT_HOUR, "") + ":" + preferences.getString(Const.WHOLE_DAY_EVENT_MINUTE, ""));
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
                finish();
            }
            case R.id.whole_day_event_time_relative_layout: {
                pickerDialog.show();
                break;
            }

            case R.id.btnSubmit: {
                hour = pickerDialog.wheelTimePicker.hour;
                minute = pickerDialog.wheelTimePicker.minute;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(Const.WHOLE_DAY_EVENT_HOUR, hour);
                editor.putString(Const.WHOLE_DAY_EVENT_MINUTE, minute);
                wholeDayEventTime.setText(hour + ":" + minute);
                editor.commit();
                pickerDialog.dismiss();
                AppUser user = new AppUser();
                user.id = preferences.getString(Const.USER_ID, null);
                user.allDayReminderTime = hour + minute;
                apiInterface.updateUserInfo(user).enqueue(new Callback<GetVerificationModel>() {
                    @Override
                    public void onResponse(Call<GetVerificationModel> call, Response<GetVerificationModel> response) {
                        GetVerificationModel model = response.body();
                        Logger.d("zzw", "update whole day reminder time ok " + call);
                        if (model.code != 200) {
                            Toast.makeText(getApplicationContext(), "update failed", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetVerificationModel> call, Throwable t) {
                        Logger.d("zzw", "update whole day reminder time fail " + call);
                    }
                });
                break;
            }
            case R.id.btnCancel: {
                pickerDialog.dismiss();
                break;
            }
        }
    }
}
