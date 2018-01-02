package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chatuidemo.ui.BaseActivity;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.widget.dialog.CheckInDialog;
import com.riking.calendar.widget.dialog.CheckInFailDialog;

import java.util.Map;

public class UserActivity extends BaseActivity {
    TextView userName;
    LinearLayout loginLinearLayout;
    TextView userComment;
    ImageView toUserInfoIm;
    TextView checkInTv;
    ImageView myPhoto;
    RelativeLayout userInfoRelativeLayout;
    LinearLayout myRepliesLayout;
    LinearLayout followMeLayout;
    LinearLayout myFollowLayout;
    AppUserResp currentUser;

    TextView myFollowNumbTv;
    TextView followingMeNumbTv;
    TextView myAnswerNumbTv;

    View trendLayout;
    View followLayout;
    View collecLayout;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_user);
        initView();
        initListener();
    }

    private void initView() {
        collecLayout = findViewById(R.id.my_favorite_layout);
        followLayout = findViewById(R.id.my_follow_layout);
        trendLayout = findViewById(R.id.my_trend_layout);
        myFollowLayout = findViewById(R.id.my_follow_person_layout);
        followMeLayout = findViewById(R.id.my_follower_layout);
        myRepliesLayout = findViewById(R.id.my_replyes);
        userInfoRelativeLayout = findViewById(R.id.user_info_relative_layout);
        userName = findViewById(R.id.user_name);
        loginLinearLayout = findViewById(R.id.login_linear_layout);
        userComment = findViewById(R.id.user_comment);
        toUserInfoIm = findViewById(R.id.to_info);
        checkInTv = findViewById(R.id.check_in_button);
        myPhoto = findViewById(R.id.user_icon);
        myFollowNumbTv = findViewById(R.id.my_follow_person_num);
        followingMeNumbTv = findViewById(R.id.following_me_num);
        myAnswerNumbTv = findViewById(R.id.my_answer_num_tv);
    }

    private void initListener() {
        collecLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                ZGoto.to(MyCollectActivity.class);
            }
        });
        userInfoRelativeLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                if (ZPreference.isLogin()) {
                    startActivityForResult(new Intent(UserActivity.this, UserInfoActivity.class), CONST.UPDATE_USER_INFO_REQUES);
                } else {
                    startActivity((new Intent(UserActivity.this, LoginNavigateActivity.class)));
//                    startActivity(new Intent(getContext(), HyphenateLoginActivity.class));
                }
            }
        });

        //set my replies click listener
        myRepliesLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ZGoto.to(MyRepliesActivity.class);
            }
        });

        // go to following me activity on click
        followMeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ZGoto.to(MyFollowersActivity.class);
            }
        });

        //go to my follow
        myFollowLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                Intent i = new Intent(UserActivity.this, MyFavoritesUserActivity.class);
                ZGoto.to(i);
            }
        });

        //go to my trend
        trendLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                ZGoto.to(MyStateActivity.class);
            }
        });

        followLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                ZGoto.to(MyFollowActivity.class);
            }
        });
    }
}
