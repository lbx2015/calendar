package com.riking.calendar.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riking.calendar.BuildConfig;
import com.riking.calendar.R;
import com.riking.calendar.activity.FeedBackActivity;
import com.riking.calendar.activity.LoginNavigateActivity;
import com.riking.calendar.activity.MyFavoritesUserActivity;
import com.riking.calendar.activity.MyFollowActivity;
import com.riking.calendar.activity.MyFollowersActivity;
import com.riking.calendar.activity.MyRepliesActivity;
import com.riking.calendar.activity.MyStateActivity;
import com.riking.calendar.activity.SettingActivity;
import com.riking.calendar.activity.UserInfoActivity;
import com.riking.calendar.activity.WebviewActivity;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithFail;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.resp.AppUserResp;
import com.riking.calendar.pojo.server.UserOperationInfo;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.task.LoadUserImageTask;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.MarketUtils;
import com.riking.calendar.util.StringUtil;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;
import com.riking.calendar.util.ZR;
import com.riking.calendar.widget.dialog.CheckInDialog;
import com.riking.calendar.widget.dialog.CheckInFailDialog;

import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class UserInfoFragment extends Fragment implements OnClickListener {
    TextView userName;
    LinearLayout loginLinearLayout;
    TextView userComment;
    ImageView toUserInfoIm;
    TextView checkInTv;
    TextView notLoginTv;
    RelativeLayout setLayout;
    ImageView myPhoto;
    RelativeLayout userInfoRelativeLayout;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    int loginState;
    View v;
    LinearLayout myRepliesLayout;
    LinearLayout followMeLayout;
    LinearLayout myFollowLayout;
    AppUserResp currentUser;
    View suggestionLayout;

    TextView myFollowNumbTv;
    TextView followingMeNumbTv;
    TextView myAnswerNumbTv;

    View trendLayout;
    View followLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (v != null) {
            return v;
        }
        v = inflater.inflate(R.layout.my_fragment, container, false);
        init();

        if (ZPreference.isLogin()) {
            toUserInfoIm.setVisibility(View.VISIBLE);
            notLoginTv.setVisibility(View.GONE);
            loginLinearLayout.setVisibility(View.VISIBLE);
            loginState = 1;
            currentUser = ZPreference.getCurrentLoginUser();
            userName.setText(currentUser.userName);
            userComment.setText(currentUser.descript);

            //load the user image
            ZR.setUserImage(myPhoto, currentUser.photoUrl);
        } else {
            toUserInfoIm.setVisibility(View.GONE);
            notLoginTv.setVisibility(View.VISIBLE);
            loginLinearLayout.setVisibility(View.GONE);
            loginState = 0;
        }
        return v;
    }

    private void init() {
        initViews();
        initEvents();
        loadUserOperationInfo();
    }

    private void loadUserOperationInfo() {
        UserParams u = new UserParams();
        APIClient.getUserOperationInfo(u, new ZCallBack<ResponseModel<UserOperationInfo>>() {
            @Override
            public void callBack(ResponseModel<UserOperationInfo> response) {
                UserOperationInfo u = response._data;
                myFollowNumbTv.setText(ZR.getNumberString(u.followNum));
                followingMeNumbTv.setText(ZR.getNumberString(u.fansNum));
                myAnswerNumbTv.setText(ZR.getNumberString(u.answerNum));
                //already signed in
                if (u.signStatus == 1) {
                    checkInTv.setText("已签到");
                    checkInTv.setClickable(false);
                    checkInTv.setEnabled(false);
                } else {
                    checkInTv.setText("签到");
                    checkInTv.setClickable(true);
                    checkInTv.setEnabled(true);
                }
            }
        });
    }

    private void initViews() {
        followLayout = v.findViewById(R.id.my_follow_layout);
        trendLayout = v.findViewById(R.id.my_trend_layout);
        suggestionLayout = v.findViewById(R.id.suggestion_layout);
        myFollowLayout = v.findViewById(R.id.my_follow_person_layout);
        followMeLayout = v.findViewById(R.id.my_follower_layout);
        myRepliesLayout = v.findViewById(R.id.my_replyes);
        setLayout = v.findViewById(R.id.set_layout);
        userInfoRelativeLayout = v.findViewById(R.id.user_info_relative_layout);
        userName = v.findViewById(R.id.user_name);
        loginLinearLayout = v.findViewById(R.id.login_linear_layout);
        userComment = v.findViewById(R.id.user_comment);
        toUserInfoIm = v.findViewById(R.id.to_info);
        checkInTv = v.findViewById(R.id.check_in_button);
        notLoginTv = v.findViewById(R.id.not_login_button);
        myPhoto = v.findViewById(R.id.user_icon);
        myFollowNumbTv = v.findViewById(R.id.my_follow_person_num);
        followingMeNumbTv = v.findViewById(R.id.following_me_num);
        myAnswerNumbTv = v.findViewById(R.id.my_answer_num_tv);
    }

    private void initEvents() {
        setLayout.setOnClickListener(this);
        userInfoRelativeLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                if (ZPreference.isLogin()) {
                    startActivityForResult(new Intent(getContext(), UserInfoActivity.class), CONST.UPDATE_USER_INFO_REQUES);
                } else {
                    startActivity((new Intent(getContext(), LoginNavigateActivity.class)));
//                    startActivity(new Intent(getContext(), HyphenateLoginActivity.class));
                }
            }
        });

        //set sign in click listener

        checkInTv.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                UserParams userParams = new UserParams();
                APIClient.signIn(userParams, new ZCallBackWithFail<ResponseModel<Map<String, Integer>>>() {
                    @Override
                    public void callBack(ResponseModel<Map<String, Integer>> response) throws Exception {
                        Map<String, Integer> maps = response._data;
                        if (maps == null || failed) {
                            CheckInFailDialog dialog = new CheckInFailDialog(checkInTv.getContext());
                            dialog.show();
                        } else {
                            checkInTv.setText("已签到");
                            checkInTv.setClickable(false);
                            checkInTv.setEnabled(false);
                            CheckInDialog dialog = new CheckInDialog(checkInTv.getContext());
                            dialog.setExperience(maps == null ? 0 : maps.get("integral"));
                            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                            dialog.setContentView(layoutInflater.inflate(R.layout.dialog_check_in, null));
                            dialog.zEnterImageView = dialog.findViewById(R.id.sign_in_success_img);
                            dialog.zEnterImageView.text = "+" + (
                                    maps == null ? 0 : maps.get("signIntegral"));
                            dialog.show();
                            dialog.zEnterImageView.text = "+" + maps.get("signIntegral");
                            dialog.show();
                        }
                    }
                });
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
                Intent i = new Intent(getContext(), MyFavoritesUserActivity.class);
                ZGoto.to(i);
            }
        });
        suggestionLayout.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                ZGoto.to(FeedBackActivity.class);
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

    @Override
    public void onStart() {
        super.onStart();
        if (ZPreference.isLogin()) {
            notLoginTv.setVisibility(View.GONE);
            loginLinearLayout.setVisibility(View.VISIBLE);
            //Login state changed.
            if ((loginState ^ 1) == 1) {
                //refresh the activity
                getActivity().recreate();
                loginState = 1;
                AppUserResp currentUser = ZPreference.getCurrentLoginUser();
                userName.setText(currentUser.userName);
                userComment.setText(currentUser.descript);

                String imageUrl = currentUser.photoUrl;
                if (imageUrl != null && imageUrl.length() > 0) {
                    LoadUserImageTask myTask = new LoadUserImageTask();
                    myTask.imageView = myPhoto;
                    myTask.execute(imageUrl);
                }
            }

        } else {
            notLoginTv.setVisibility(View.VISIBLE);
            loginLinearLayout.setVisibility(View.GONE);
            if ((loginState ^ 1) == 0) {
                //refresh the activity
                getActivity().recreate();
                loginState = 0;
                userName.setText(getString(R.string.not_register));
                myPhoto.setImageDrawable(getResources().getDrawable(R.drawable.user_icon_head_notlogin));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_layout: {
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            }

            case R.id.about_relative_layout: {
                apiInterface.getAboutHtml(BuildConfig.VERSION_NAME).enqueue(new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        Intent i = new Intent(getContext(), WebviewActivity.class);
                        i.putExtra(CONST.WEB_URL, response._data);
                        startActivity(i);
                    }
                });
                break;
            }
            case R.id.comment_root: {
                MarketUtils.launchAppDetail(BuildConfig.APPLICATION_ID, "");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure the request was successful
        if (resultCode != RESULT_CANCELED && requestCode == CONST.UPDATE_USER_INFO_REQUES) {
            Bundle b = data.getExtras();
            currentUser = ZPreference.getCurrentLoginUser();
            String newName = b.getString(CONST.USER_NAME);
            if (!StringUtil.isEmpty(newName)) {
                userName.setText(currentUser.userName);
            }
            //update user image
            if (!StringUtil.isEmpty(b.getString(CONST.USER_IMAGE_URL))) {
                ZR.setUserImage(myPhoto, b.getString(CONST.USER_IMAGE_URL));
            }

            //update user description
            if (!StringUtil.isEmpty(b.getString(CONST.USER_COMMENTS))) {
                userComment.setText(b.getString(CONST.USER_COMMENTS));
            }
        }
    }
}
