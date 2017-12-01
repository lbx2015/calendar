package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.chatuidemo.ui.BaseActivity;
import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.server.OtherUserResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;

public class UserActivity extends BaseActivity {
    TextView userName;
    LinearLayout loginLinearLayout;
    TextView userComment;
    ImageView toUserInfoIm;
    ImageView myPhoto;
    LinearLayout myRepliesLayout;
    LinearLayout followMeLayout;
    LinearLayout myFollowLayout;

    TextView myFollowNumbTv;
    TextView followingMeNumbTv;
    TextView myAnswerNumbTv;

    View trendLayout;
    View followLayout;
    View collecLayout;

    //text view title
    TextView followPersonTv;
    TextView fansTv;
    TextView answerTv;
    TextView trendTv;
    TextView myFollowTv;
    TextView myFavoriteTv;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_user);
        initView();
        initListener();
    }

    private void initView() {
        //follow persons
        followPersonTv = findViewById(R.id.follow_person_tv);
        fansTv = findViewById(R.id.fans_tv);
        answerTv = findViewById(R.id.answer_tv);
        trendTv = findViewById(R.id.my_trend_tv);
        myFollowTv = findViewById(R.id.my_follow_tv);
        myFavoriteTv = findViewById(R.id.my_favorite_tv);

        collecLayout = findViewById(R.id.my_favorite_layout);
        followLayout = findViewById(R.id.my_follow_layout);
        trendLayout = findViewById(R.id.my_trend_layout);
        myFollowLayout = findViewById(R.id.my_follow_person_layout);
        followMeLayout = findViewById(R.id.my_follower_layout);
        myRepliesLayout = findViewById(R.id.my_replyes);
        userName = findViewById(R.id.user_name);
        loginLinearLayout = findViewById(R.id.login_linear_layout);
        userComment = findViewById(R.id.user_comment);
        toUserInfoIm = findViewById(R.id.to_info);
        myPhoto = findViewById(R.id.user_icon);
        myFollowNumbTv = findViewById(R.id.my_follow_person_num);
        followingMeNumbTv = findViewById(R.id.following_me_num);
        myAnswerNumbTv = findViewById(R.id.my_answer_num_tv);
    }

    private void setUserData(final OtherUserResp u) {
        myFollowNumbTv.setText(ZR.getNumberString(u.followNum));
        followingMeNumbTv.setText(ZR.getNumberString(u.fansNum));
        myAnswerNumbTv.setText(ZR.getNumberString(u.answerNum));
        userName.setText(u.userName);
        ZR.setUserName(userName, u.userName, u.grade);
        userComment.setText(u.descript);

        //load the user image
        ZR.setUserImage(myPhoto, u.photoUrl);

        if (u.userId.equals(ZPreference.getUserId())) {
            followPersonTv.setText("我关注的人");
            fansTv.setText("我的粉丝");
            answerTv.setText("我的回答");
            trendTv.setText("我的动态");
            myFollowTv.setText("我的关注");
            myFavoriteTv.setText("我的收藏");
        } else if (u.sex == 1) {
            followPersonTv.setText("他关注的人");
            fansTv.setText("他的粉丝");
            answerTv.setText("他的回答");
            trendTv.setText("他的动态");
            myFollowTv.setText("他的关注");
            myFavoriteTv.setText("他的收藏");
        } else if ((u.sex == 0)) {
            followPersonTv.setText("她关注的人");
            fansTv.setText("她的粉丝");
            answerTv.setText("她的回答");
            trendTv.setText("她的动态");
            myFollowTv.setText("她的关注");
            myFavoriteTv.setText("她的收藏");
        }

        //set click listener
        collecLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                Intent i = new Intent(UserActivity.this, MyCollectActivity.class);
                i.putExtra(CONST.USER_ID, u.userId);
                ZGoto.to(i);
            }
        });

        //set my replies click listener
        myRepliesLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, MyRepliesActivity.class);
                i.putExtra(CONST.USER_ID, u.userId);
                ZGoto.to(i);
            }
        });

        // go to following me activity on click
        followMeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, MyFollowersActivity.class);
                i.putExtra(CONST.USER_ID, u.userId);
                ZGoto.to(i);
            }
        });

        //go to my follow
        myFollowLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                Intent i = new Intent(UserActivity.this, MyFavoritesUserActivity.class);
                i.putExtra(CONST.USER_ID, u.userId);
                ZGoto.to(i);
            }
        });

        //go to my trend
        trendLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                Intent i = new Intent(UserActivity.this, MyStateActivity.class);
                i.putExtra(CONST.USER_ID, u.userId);
                ZGoto.to(i);
            }
        });

        followLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                Intent i = new Intent(UserActivity.this, MyFollowActivity.class);
                i.putExtra(CONST.USER_ID, u.userId);
                ZGoto.to(i);
            }
        });
    }

    private void initListener() {
        UserParams params = new UserParams();
        params.toUserId = getIntent().getStringExtra(CONST.USER_ID);
        //get other user info
        APIClient.getOtherPersonInfo(params, new ZCallBack<ResponseModel<OtherUserResp>>() {
            @Override
            public void callBack(ResponseModel<OtherUserResp> response) {
                OtherUserResp u = response._data;
                setUserData(u);
            }
        });
    }
}
