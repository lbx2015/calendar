package com.riking.calendar.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.hyphenate.chatuidemo.ui.HyphenateLoginActivity;
import com.riking.calendar.BuildConfig;
import com.riking.calendar.R;
import com.riking.calendar.activity.LoginActivity;
import com.riking.calendar.activity.LoginNavigateActivity;
import com.riking.calendar.activity.SettingActivity;
import com.riking.calendar.activity.UserInfoActivity;
import com.riking.calendar.activity.WebviewActivity;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.retrofit.APIClient;
import com.riking.calendar.retrofit.APIInterface;
import com.riking.calendar.task.LoadUserImageTask;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.FileUtil;
import com.riking.calendar.util.MarketUtils;

/**
 * Created by zw.zhang on 2017/7/11.
 */

public class FourthFragment extends Fragment implements OnClickListener {
    SharedPreferences sharedPreferences;
    TextView userName;
    ImageView myPhoto;
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
    int loginState;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(v!=null){
            return v;
        }
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences(CONST.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        View v = inflater.inflate(R.layout.fourth_fragment, container, false);
        v.findViewById(R.id.my_photo_layout).setOnClickListener(this);
        v.findViewById(R.id.set_layout).setOnClickListener(this);
        v.findViewById(R.id.about_relative_layout).setOnClickListener(this);
        v.findViewById(R.id.comment_root).setOnClickListener(this);


        userName = (TextView) v.findViewById(R.id.user_name);
        myPhoto = (ImageView) v.findViewById(R.id.my_photo);
        if (sharedPreferences.getBoolean(CONST.IS_LOGIN, false)) {
            loginState = 1;
            userName.setText(sharedPreferences.getString(CONST.USER_NAME, null) + "\n" +
                    sharedPreferences.getString(CONST.USER_COMMENTS, ""));

            String imageUrl = sharedPreferences.getString(CONST.USER_IMAGE_URL, null);
            String imageName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            if (FileUtil.imageExists(imageName)) {
                Logger.d("zzw", "no need load url: " + imageName);
                Bitmap bitmap = BitmapFactory.decodeFile(FileUtil.getImageFilePath(imageName));
                myPhoto.setImageBitmap(bitmap);

            } else if (imageUrl != null && imageUrl.length() > 0) {
                Logger.d("zzw", " load url: " + imageUrl);
                LoadUserImageTask myTask = new LoadUserImageTask();
                myTask.imageView = myPhoto;
                myTask.execute(imageUrl);
            }
        } else {
            loginState = 0;
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sharedPreferences.getBoolean(CONST.IS_LOGIN, false)) {
            //Login state changed.
            if ((loginState ^ 1) == 1) {
                //refresh the activity
                getActivity().recreate();
                loginState = 1;
                userName.setText(sharedPreferences.getString(CONST.USER_NAME, null) + "\n" +
                        sharedPreferences.getString(CONST.USER_COMMENTS, ""));

                String imageUrl = sharedPreferences.getString(CONST.USER_IMAGE_URL, null);
                if (imageUrl != null && imageUrl.length() > 0) {
                    LoadUserImageTask myTask = new LoadUserImageTask();
                    myTask.imageView = myPhoto;
                    myTask.execute(imageUrl);
                }
            }
        } else {
            if ((loginState ^ 1) == 0) {
                //refresh the activity
                getActivity().recreate();
                loginState = 0;
                userName.setText(getString(R.string.not_register));
                myPhoto.setImageDrawable(getResources().getDrawable(R.drawable.default_user_icon));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_photo_layout: {

                if (sharedPreferences.getBoolean(CONST.IS_LOGIN, false)) {
                    startActivity(new Intent(getContext(), UserInfoActivity.class));
                } else {
                    startActivity((new Intent(getContext(), LoginNavigateActivity.class)));
//                    startActivity(new Intent(getContext(), HyphenateLoginActivity.class));
                }
                break;
            }
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
}
