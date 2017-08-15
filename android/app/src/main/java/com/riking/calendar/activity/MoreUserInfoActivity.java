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

public class MoreUserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences preference;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    TextView sexTextView;
    TextView addressTextView;
    TextView birthDayTextView;
    TextView commentsTextView;
    View commentsRelativeLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preference = getSharedPreferences(Const.PREFERENCE_FILE_NAME, MODE_PRIVATE);
        setContentView(R.layout.activity_more_user_info);
        sexTextView = (TextView) findViewById(R.id.sex);
        addressTextView = (TextView) findViewById(R.id.address);
        birthDayTextView = (TextView) findViewById(R.id.birthday);
        commentsTextView = (TextView) findViewById(R.id.comments);

        addressTextView.setText(preference.getString(Const.USER_ADDRESS, ""));
        commentsTextView.setText(preference.getString(Const.USER_COMMENTS, ""));
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
