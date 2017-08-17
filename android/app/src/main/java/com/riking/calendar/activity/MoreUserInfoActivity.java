package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.ldf.calendar.Const;
import com.riking.calendar.R;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.AppUser;
import com.riking.calendar.pojo.GetVerificationModel;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.widget.dialog.BirthdayPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by zw.zhang on 2017/8/5.
 */

public class MoreUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences preference;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    TextView sexTextView;
    TextView addressTextView;
    TextView birthDayTextView;
    TextView commentsTextView;
    View commentsRelativeLayout;
    View addressRelativeLayout;
    View birthDayRelative;
    BirthdayPickerDialog datePickerDialog;
    //time
    Calendar calendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_more_user_info);
        sexTextView = (TextView) findViewById(R.id.sex);
        addressTextView = (TextView) findViewById(R.id.address);
        birthDayTextView = (TextView) findViewById(R.id.birthday);
        commentsTextView = (TextView) findViewById(R.id.comments);
        addressRelativeLayout = findViewById(R.id.address_relative_layout);
        birthDayRelative = findViewById(R.id.birthday_relative_layout);

        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSubmit: {
                        calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, Integer.parseInt(datePickerDialog.wheelDatePicker.year));
                        calendar.set(Calendar.MONTH, Integer.parseInt(datePickerDialog.wheelDatePicker.month) - 1);
                        calendar.set(Calendar.DATE, Integer.parseInt(datePickerDialog.wheelDatePicker.day) - 1);
                        AppUser user = new AppUser();
                        user.id = preference.getString(Const.USER_ID, null);
                        user.birthday = new SimpleDateFormat(Const.yyyyMMdd).format(calendar.getTime());
                        apiInterface.updateUserBirthDay(user).enqueue(new ZCallBack<ResponseModel<AppUser>>() {
                            @Override
                            public void callBack(ResponseModel<AppUser> response) {
                                Logger.d("zzw", "request success");
                                SharedPreferences.Editor editor = preference.edit();
                                String birthDay = new SimpleDateFormat(Const.birthDayFormat).format(calendar.getTime());
                                editor.putString(Const.USER_BIRTHDAY, birthDay);
                                editor.commit();
                                birthDayTextView.setText(birthDay);
                            }
                        });
                        datePickerDialog.dismiss();
                        break;
                    }
                    case R.id.btnCancel: {
                        datePickerDialog.dismiss();
                        break;
                    }
                }
            }
        };

        birthDayRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datePickerDialog == null) {
                    datePickerDialog = new BirthdayPickerDialog(MoreUserInfoActivity.this);
                    datePickerDialog.btnSubmit.setOnClickListener(listener);
                    datePickerDialog.btnCancel.setOnClickListener(listener);
                }
                datePickerDialog.show();
            }
        });

        addressRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreUserInfoActivity.this);
                builder.setTitle(getString(R.string.address));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(MoreUserInfoActivity.this).inflate(R.layout.edit_user_name_dialog, null, false);
                // Set up the input
                final AutoCompleteTextView input = (AutoCompleteTextView) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                String addressText = preference.getString(Const.USER_ADDRESS, "");
                input.setText(addressText);
                input.setSelection(addressText.length());

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Editable editable = input.getText();
                        if (editable == null) {
                            return;
                        }
                        String departName = input.getText().toString();
                        if (departName.length() > 0) {
                            addressTextView.setText(departName);
                            AppUser user = new AppUser();
                            user.address = departName;
                            user.id = preference.getString(Const.USER_ID, null);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString(Const.USER_ADDRESS, departName);
                            //save the changes.
                            editor.commit();

                            apiInterface.updateUserInfo(user).enqueue(new Callback<GetVerificationModel>() {
                                @Override
                                public void onResponse(Call<GetVerificationModel> call, Response<GetVerificationModel> response) {
                                    GetVerificationModel user = response.body();
                                    Logger.d("zzw", "update user : " + user);
                                }

                                @Override
                                public void onFailure(Call<GetVerificationModel> call, Throwable t) {
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        addressTextView.setText(preference.getString(Const.USER_ADDRESS, ""));
        commentsTextView.setText(preference.getString(Const.USER_COMMENTS, ""));
        birthDayTextView.setText(preference.getString(Const.USER_BIRTHDAY, ""));
        commentsRelativeLayout = findViewById(R.id.comments_relative_layout);
        commentsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreUserInfoActivity.this);
                builder.setTitle(getString(R.string.user_comments));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(MoreUserInfoActivity.this).inflate(R.layout.edit_user_name_dialog, null, false);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                input.setText(preference.getString(Const.USER_COMMENTS, ""));
                input.setSelection(preference.getString(Const.USER_COMMENTS, "").length());
                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Editable editable = input.getText();
                        if (editable == null) {
                            return;
                        }
                        String newComments = input.getText().toString();
                        if (newComments.length() > 0) {
                            commentsTextView.setText(newComments);
                            AppUser user = new AppUser();
                            user.remark = newComments;
                            user.id = preference.getString(Const.USER_ID, null);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString(Const.USER_COMMENTS, newComments);
                            //save the changes.
                            editor.commit();

                            apiInterface.updateUserInfo(user).enqueue(new Callback<GetVerificationModel>() {
                                @Override
                                public void onResponse(Call<GetVerificationModel> call, Response<GetVerificationModel> response) {
                                    GetVerificationModel user = response.body();
                                    Logger.d("zzw", "update user : " + user);
                                }

                                @Override
                                public void onFailure(Call<GetVerificationModel> call, Throwable t) {
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        if (preference.getInt(Const.USER_SEX, 1) == 1) {
            sexTextView.setText(getString(R.string.male));
        } else {
            sexTextView.setText(getString(R.string.female));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
        }
    }
}
