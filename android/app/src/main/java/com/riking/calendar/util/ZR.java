package com.riking.calendar.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.app.GlideApp;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithoutProgress;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.retrofit.APIClient;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zw.zhang on 2017/7/14.
 */

public class ZR {
    public static String[] emailSufixs = new String[]{"@qq.com", "@163.com", "@126.com", "@gmail.com", "@sina.com", "@hotmail.com",
            "@yahoo.cn", "@sohu.com", "@foxmail.com", "@139.com", "@yeah.net", "@vip.qq.com", "@vip.sina.com"};
    public static String jumpClass;

    static {
        APIClient.getAllEmailSuffix(new ZCallBackWithoutProgress<ResponseModel<List<String>>>() {
            @Override
            public void callBack(ResponseModel<List<String>> response) {
                emailSufixs = new String[response._data.size()];
                for (int i = 0; i < response._data.size(); i++) {
                    emailSufixs[i] = response._data.get(i);
                }
            }
        });
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static float convertDpToPx(float dp) {
        return dp * MyApplication.APP.getResources().getDisplayMetrics().density;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPxToDp(Context context, float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    public static String getDeviceId() {
        TelephonyManager tManager = (TelephonyManager) MyApplication.mCurrentActivity.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(MyApplication.mCurrentActivity, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return tManager.getDeviceId();
        }
        return "";
    }

    @ColorInt
    public static int getColor(@ColorRes int resId) {
        return MyApplication.APP.getResources().getColor(resId);
    }

    public static Drawable getDrawable(@DrawableRes int resId) {
        return MyApplication.APP.getResources().getDrawable(resId);
    }

    public static String getNumberString(long number) {
        String[] suffix = new String[]{"", "k", "m", "b", "t"};
        int MAX_LENGTH = 4;
        String r = new DecimalFormat("##0E0").format(number);
        System.out.println(r);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }
        return r;
    }

    public static void setUserImage(ImageView v, String imageUrl) {
        RequestOptions options = new RequestOptions();
        //if fail user the default user icon
        Glide.with(v.getContext()).load(imageUrl)
                .apply(options.fitCenter().placeholder(R.drawable.user_icon_head_notlogin))
                .into(v);
    }

    public static void setCircleUserImage(ImageView v, String imageUrl) {
        Glide.with(v.getContext()).load(imageUrl).apply(new RequestOptions().circleCrop().placeholder(R.drawable.user_icon_head_notlogin)).into(v);
    }

    public static void setCircleUserImage(ImageView v, Bitmap bitmap) {
        Glide.with(v.getContext()).load(bitmap).apply(new RequestOptions().circleCrop().placeholder(R.drawable.user_icon_head_notlogin)).into(v);
    }

    public static void setImage(ImageView v, String imageUrl) {
        GlideApp.with(v.getContext()).load(imageUrl).centerCrop().placeholder(R.drawable.banner).into(v);
    }

    public static void setUserName(TextView userNameTv, String name, int grand) {
        userNameTv.setText(name);
        if (grand == 3) {
            userNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_icon_grade_v3, 0);
        } else if (grand == 4) {
            userNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_icon_grade_v4, 0);
        } else if (grand == 5) {
            userNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.com_icon_grade_v5, 0);
        } else {
            userNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    public static void showPersonFollowStatus(View followButton, TextView followTv, int isFollow) {
        if (isFollow == 0) {
            followTv.setText("关注");
            followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
            followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border));
        } else if (isFollow == 1) {
            followTv.setText("已关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            followTv.setTextColor(ZR.getColor(R.color.color_999999));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border_gray));
        } else if (isFollow == 2) {
            followTv.setText("互相关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            followTv.setTextColor(ZR.getColor(R.color.color_999999));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border_gray));
        }
    }

    public static void setFollowPersonClickListner(final AppUserResult user, final View followButton, final TextView followTv) {
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TQuestionParams params = new TQuestionParams();
                params.attentObjId = user.userId;
                //follow user
                params.objType = 3;
                //followed
                if (user.isFollow == 0) {
                    params.enabled = 1;
                } else {
                    params.enabled = 0;
                }

                APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        int status = Integer.parseInt(response._data);
                        user.isFollow = status;
                        if (user.isFollow == 0) {
                            ZToast.toast("取消关注");
                        } else {
                            ZToast.toast("关注成功");
                        }
                        showPersonFollowStatus(followButton, followTv, status);
                    }
                });
            }
        });
    }

    public static void setTopicFollowClickListener(final Topic topic, final View followButton, final TextView followTv) {
        followButton.setOnClickListener(new ZClickListenerWithLoginCheck() {
            @Override
            public void click(View v) {
                //adding null protection
                if (topic == null) {
                    return;
                }
                final TQuestionParams params = new TQuestionParams();
                params.attentObjId = topic.topicId;
                //topic
                params.objType = 2;
                //followed
                if (topic.isFollow == 1) {
                    params.enabled = 0;
                } else {
                    params.enabled = 1;
                }

                APIClient.follow(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        topic.isFollow = params.enabled;
                        if (topic.isFollow == 1) {
                            ZToast.toast("关注成功");
                        } else {
                            ZToast.toast("取消关注");
                        }
                        showTopicFollowStatus(followButton, followTv, params.enabled);
                    }
                });
            }
        });
    }

    public static void showTopicFollowStatus(View followButton, TextView followTv, int isFollow) {
        if (isFollow == 0) {
            followTv.setText("关注");
            followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
            followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border));
        } else {
            followTv.setText("已关注");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            followTv.setTextColor(ZR.getColor(R.color.color_999999));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border_gray));
        }
    }

    public void getDensity(Activity activity) {
        float density = activity.getResources().getDisplayMetrics().density;
        if (density > 4.0f) {
            //it is a xxxhdpi display
            //load a extra extra extra high resolution image (600x600px, for example)
        } else if (density > 3.0f) {
            Log.d("zzw", "it is a xxhdpi display");
            //it is a xxhdpi display
            //load a extra extra high resolution image (450x450px, for example)
        } else if (density > 2.0f) {
            Log.d("zzw", "it is a xhdpi display");
            //it is a xhdpi display
            //load a extra high resolution image (300x300px, for example)
        } else if (density > 1.5f) {
            Log.d("zzw", "it is a hdpi display");
            //it is a hdpi display
            //load a high resolution image (200x200px, for example)
        } else if (density > 1.0f) {
            //it is a mdpi display
            //load a medium resolution image (150x150px, for example)
        } else if (density > 0.75f) {
            //it is a ldpi display
            //load a low resolution image (100x100px, for example)
        }
    }
}
