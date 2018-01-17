package com.riking.calendar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riking.calendar.R;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.server.OtherUserResp;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.util.ZToast;

public class UserActivity extends AppCompatActivity {
    public TextView followTv;
    public View followButton;
    TextView userName;
    LinearLayout loginLinearLayout;
    TextView userComment;
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
    ImageView trendIv;
    ImageView followIv;
    ImageView collectIv;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_user);
        initView();
        initListener();
    }

    public void clickBack(final View view) {
        onBackPressed();
    }

    private void initView() {
        trendIv = findViewById(R.id.trend_image);
        followIv = findViewById(R.id.follow_image);
        collectIv = findViewById(R.id.favorite_image);
        followButton = findViewById(R.id.follow_button);
        followTv = findViewById(R.id.follow_text);
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
        myPhoto = findViewById(R.id.user_icon);
        myFollowNumbTv = findViewById(R.id.my_follow_person_num);
        followingMeNumbTv = findViewById(R.id.following_me_num);
        myAnswerNumbTv = findViewById(R.id.my_answer_num_tv);
    }

    private void showInvited(TextView followTv, int isFollow) {
        if (isFollow == 0) {
            followTv.setText("关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus_w, 0, 0, 0);
            followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
        } else if (isFollow == 1) {
            followTv.setText("已关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else if (isFollow == 2) {
            followTv.setText("互相关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
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
            followButton.setVisibility(View.GONE);
        } else {
            followButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TQuestionParams params = new TQuestionParams();
                    params.attentObjId = u.userId;
                    //follow person
                    params.objType = 3;
                    //followed
                    if (u.isFollow == 1) {
                        params.enabled = 0;
                    } else {
                        params.enabled = 1;
                    }

                    APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                        @Override
                        public void callBack(ResponseModel<String> response) {
                            u.isFollow = params.enabled;
                            if (u.isFollow == 1) {
                                ZToast.toast("关注成功");
                            } else {
                                ZToast.toast("取消关注");
                            }
                            showInvited(followTv, u.isFollow);
                        }
                    });
                }
            });

            if (u.sex == 1) {
                followButton.setVisibility(View.VISIBLE);
                showInvited(followTv, u.isFollow);
                followPersonTv.setText("他关注的人");
                fansTv.setText("他的粉丝");
                answerTv.setText("他的回答");
                trendTv.setText("他的动态");
                myFollowTv.setText("他的关注");
                myFavoriteTv.setText("他的收藏");
            } else if ((u.sex == 0)) {
                followButton.setVisibility(View.VISIBLE);
                showInvited(followTv, u.isFollow);
                followPersonTv.setText("她关注的人");
                fansTv.setText("她的粉丝");
                answerTv.setText("她的回答");
                trendTv.setText("她的动态");
                myFollowTv.setText("她的关注");
                myFavoriteTv.setText("她的收藏");
            }

            if (u.checkMyDynamicState == 1) {
                ZR.setImage(trendIv, R.drawable.user_icon_trends);
                trendTv.setTextColor(ZR.getColor(R.color.color_222222));
            } else {
                ZR.setImage(trendIv, R.drawable.user_icon_trends_d);
                trendTv.setTextColor(ZR.getColor(R.color.color_999999));
            }
            if (u.checkMyFollowState == 1) {
                ZR.setImage(followIv, R.drawable.user_icon_follow);
                myFollowTv.setTextColor(ZR.getColor(R.color.color_222222));
            } else {
                ZR.setImage(followIv, R.drawable.user_icon_follow_d);
                myFollowTv.setTextColor(ZR.getColor(R.color.color_999999));
            }
            if (u.checkMyCollectState == 1) {
                ZR.setImage(collectIv, R.drawable.user_icon_collect);
                myFavoriteTv.setTextColor(ZR.getColor(R.color.color_222222));
            } else {
                ZR.setImage(collectIv, R.drawable.user_icon_celect_d);
                myFavoriteTv.setTextColor(ZR.getColor(R.color.color_999999));

            }
        }

        //set my replies click listener
        myRepliesLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, MyRepliesActivity.class);
                i.putExtra(CONST.USER_ID, u.userId);
                i.putExtra(CONST.USER_SEX, u.sex);
                ZGoto.to(i);
            }
        });

        // go to following me activity on click
        followMeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserActivity.this, MyFollowersActivity.class);
                i.putExtra(CONST.USER_ID, u.userId);
                i.putExtra(CONST.USER_SEX, u.sex);
                ZGoto.to(i);
            }
        });

        //go to my follow
        myFollowLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                Intent i = new Intent(UserActivity.this, MyFavoritesUserActivity.class);
                i.putExtra(CONST.USER_ID, u.userId);
                i.putExtra(CONST.USER_SEX, u.sex);
                ZGoto.to(i);
            }
        });

        //set click listener
        collecLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                if (u.checkMyCollectState == 0) {
                    ZToast.toast("对方设置了隐私,对他人不可见");
                } else {
                    Intent i = new Intent(UserActivity.this, MyCollectActivity.class);
                    i.putExtra(CONST.USER_ID, u.userId);
                    i.putExtra(CONST.USER_SEX, u.sex);
                    ZGoto.to(i);
                }
            }
        });

        //go to my trend
        trendLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                if (u.checkMyDynamicState == 0) {
                    ZToast.toast("对方设置了隐私,对他人不可见");
                } else {
                    Intent i = new Intent(UserActivity.this, MyStateActivity.class);
                    i.putExtra(CONST.USER_ID, u.userId);
                    i.putExtra(CONST.USER_SEX, u.sex);
                    ZGoto.to(i);
                }
            }
        });

        followLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                if (u.checkMyFollowState == 0) {
                    ZToast.toast("对方设置了隐私,对他人不可见");
                } else {
                    Intent i = new Intent(UserActivity.this, MyFollowActivity.class);
                    i.putExtra(CONST.USER_ID, u.userId);
                    i.putExtra(CONST.USER_SEX, u.sex);
                    ZGoto.to(i);
                }
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
