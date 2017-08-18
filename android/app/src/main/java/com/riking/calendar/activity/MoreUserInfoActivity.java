package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
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
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.widget.dialog.BirthdayPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    View sexRelativeLayout;
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
        sexRelativeLayout = findViewById(R.id.sex_relative_layout);


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
                        apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                            @Override
                            public void callBack(ResponseModel<String> response) {
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

        sexRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MoreUserInfoActivity.this);
                builder.setTitle(getString(R.string.sex));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(MoreUserInfoActivity.this).inflate(R.layout.edit_sex_dialog, null, false);
                // set up radio buttons
                final AppCompatRadioButton maleButton = (AppCompatRadioButton) viewInflated.findViewById(R.id.male_button);
                final AppCompatRadioButton femaleButton = (AppCompatRadioButton) viewInflated.findViewById(R.id.female_button);

                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                final int sex = preference.getInt(Const.USER_SEX, 0);
                if (sex == 1) {
                    if (!maleButton.isChecked()) {
                        maleButton.toggle();
                    }
                }

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int newSex = -1;
                        if (maleButton.isChecked()) {
                            //male
                            newSex = 1;
                        } else {
                            //female
                            newSex = 0;
                        }
                        if (sex != newSex) {
                            final AppUser user = new AppUser();
                            user.id = preference.getString(Const.USER_ID, null);
                            user.sex = newSex;

                            apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    SharedPreferences.Editor editor = preference.edit();
                                    editor.putInt(Const.USER_SEX, user.sex);
                                    editor.commit();

                                    if (user.sex == 1) {
                                        sexTextView.setText(getString(R.string.male));
                                    } else {
                                        sexTextView.setText(getString(R.string.female));
                                    }
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
                        final String departName = input.getText().toString();
                        if (departName.length() > 0) {
                            AppUser user = new AppUser();
                            user.address = departName;
                            user.id = preference.getString(Const.USER_ID, null);

                            apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    SharedPreferences.Editor editor = preference.edit();
                                    editor.putString(Const.USER_ADDRESS, departName);
                                    //save the changes.
                                    editor.commit();
                                    addressTextView.setText(departName);
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
                        final String newComments = input.getText().toString();
                        if (newComments.length() > 0) {

                            AppUser user = new AppUser();
                            user.remark = newComments;
                            user.id = preference.getString(Const.USER_ID, null);


                            apiInterface.updateUserInfo(user).enqueue(new ZCallBack<ResponseModel<String>>() {
                                @Override
                                public void callBack(ResponseModel<String> response) {
                                    SharedPreferences.Editor editor = preference.edit();
                                    editor.putString(Const.USER_COMMENTS, newComments);
                                    //save the changes.
                                    editor.commit();
                                    commentsTextView.setText(newComments);
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
