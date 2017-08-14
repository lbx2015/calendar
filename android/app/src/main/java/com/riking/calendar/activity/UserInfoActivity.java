package com.riking.calendar.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
 * Created by zw.zhang on 2017/8/5.
 */

public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView userName;
    public TextView email;
    public TextView department;
    View userNameRelativeLayout;
    SharedPreferences preference;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    View moreUserInfoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_user_info);
        findViewById(R.id.back).setOnClickListener(this);
        userName = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        department = (TextView) findViewById(R.id.depart);
        userName.setText(preference.getString(Const.USER_NAME, null));
        email.setText(preference.getString(Const.USER_EMAIL, null));
        department.setText(preference.getString(Const.USER_DEPT, null));
        userNameRelativeLayout = findViewById(R.id.user_name_relative_layout);
        userNameRelativeLayout.setOnClickListener(this);
        moreUserInfoView = findViewById(R.id.more_user_info_relative_layout);
        moreUserInfoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this, MoreUserInfoActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back: {
                onBackPressed();
                break;
            }
            case R.id.user_name_relative_layout: {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.name));
                // I'm using fragment here so I'm using getView() to provide ViewGroup
                // but you can provide here any other instance of ViewGroup from your Fragment / Activity
                View viewInflated = LayoutInflater.from(this).inflate(R.layout.edit_user_name_dialog, null, false);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                builder.setView(viewInflated);
                String name = preference.getString(Const.USER_NAME, "");
                input.setText(name);
                input.setSelection(name.length());

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Editable editable = input.getText();
                        if (editable == null) {
                            return;
                        }
                        String newName = input.getText().toString();
                        if (newName.length() > 0) {
                            userName.setText(newName);
                            AppUser user = new AppUser();
                            user.name = newName;
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString(Const.USER_NAME, newName);
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
                break;
            }
        }
    }
}
