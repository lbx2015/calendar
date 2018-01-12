package com.riking.calendar.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.R;
import com.riking.calendar.activity.AnswerActivity;
import com.riking.calendar.activity.QuestionActivity;
import com.riking.calendar.activity.TopicActivity;
import com.riking.calendar.activity.UserActivity;
import com.riking.calendar.activity.WebviewActivity;
import com.riking.calendar.app.GlideApp;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.listener.ZCallBack;
import com.riking.calendar.listener.ZCallBackWithoutProgress;
import com.riking.calendar.listener.ZClickListenerWithLoginCheck;
import com.riking.calendar.pojo.QueryReport;
import com.riking.calendar.pojo.base.ResponseModel;
import com.riking.calendar.pojo.params.TQuestionParams;
import com.riking.calendar.pojo.params.UserParams;
import com.riking.calendar.pojo.server.AppUserResult;
import com.riking.calendar.pojo.server.Topic;
import com.riking.calendar.retrofit.APIClient;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;

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

    public static String getRegistrationId() {
        return JPushInterface.getRegistrationID(MyApplication.APP);
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
        return UUID.randomUUID().toString();
    }

    public static String getPrivateKey(String pkParams) throws UnsupportedEncodingException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] digest = md.digest(pkParams.getBytes("utf-8"));
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < digest.length; i++) {
//            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
//        }
        return new String(digest, "utf-8");
    }

    @ColorInt
    public static int getColor(@ColorRes int resId) {
        return MyApplication.APP.getResources().getColor(resId);
    }

    public static Drawable getDrawable(@DrawableRes int resId) {
        return MyApplication.APP.getResources().getDrawable(resId);
    }

    public static String getNumberString(long number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
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

    public static void setCircleUserImage(final ImageView v, final String imageUrl, final String userId) {
        setCircleUserImage(v, imageUrl);
//        Glide.with(v.getContext()).load(imageUrl).apply(new RequestOptions().circleCrop().placeholder(R.drawable.user_icon_head_notlogin)).into(v);
        //go to user activity on click
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), UserActivity.class);
                i.putExtra(CONST.USER_ID, userId);
                ZGoto.to(i);
            }
        });
    }

    public static void setTopicName(final TextView topicTv, String name, final String topicId) {
        topicTv.setText(name);
        clickTopic(topicTv, topicId);
    }

    public static void clickTopic(View v, final String topicId) {
        //go to topic activity on click
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), TopicActivity.class);
                i.putExtra(CONST.TOPIC_ID, topicId);
                ZGoto.to(i);
            }
        });
    }

    public static void setCircleTopicImage(final ImageView v, final String imageUrl, final String topicId) {
        Glide.with(v.getContext()).load(imageUrl).apply(new RequestOptions().circleCrop().placeholder(R.drawable.profile3)).into(v);
        clickTopic(v, topicId);
    }

    public static void setCircleUserImage(ImageView v, Bitmap bitmap) {
        Glide.with(v.getContext()).load(bitmap).apply(new RequestOptions().circleCrop().placeholder(R.drawable.user_icon_head_notlogin)).into(v);
    }

    public static void setImage(ImageView v, String imageUrl) {
        GlideApp.with(v.getContext()).load(imageUrl).centerCrop().placeholder(R.drawable.banner).into(v);
    }

    public static void setImage(ImageView v, @DrawableRes int imageUrl) {
        GlideApp.with(v.getContext()).load(imageUrl).centerCrop().into(v);
    }

    public static void setUserName(final TextView userNameTv, String name, int grand, final String userId) {
        setUserName(userNameTv, name, grand);
        //go to user activity on click
        userNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(userNameTv.getContext(), UserActivity.class);
                i.putExtra(CONST.USER_ID, userId);
                ZGoto.to(i);
            }
        });
    }

    public static void setUserName(final TextView userNameTv, String name, int grand) {
        userNameTv.setText(name);
        @DrawableRes int drawable;
        if (grand == 1) {
            drawable = R.drawable.com_icon_grade_v1;
        } else if (grand == 2) {
            drawable = R.drawable.com_icon_grade_v2;
        } else if (grand == 3) {
            drawable = R.drawable.com_icon_grade_v3;
        } else if (grand == 4) {
            drawable = R.drawable.com_icon_grade_v4;
        } else if (grand == 5) {
            drawable = R.drawable.com_icon_grade_v5;
        } else {
            drawable = 0;
        }
        userNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
    }

    public static void setReportName(final TextView reportNameTv, String name, int frequency, String reportBatch, final String reportId) {
        reportNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Logger.d("zzw", "report userId: " + reportId);
                QueryReport report = new QueryReport();
                report.id = reportId;
                APIClient.apiInterface.getReportDetail(report).enqueue(new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        String reportUrl = response._data;
                        Logger.d("zzw", "report Url : " + reportUrl);
                        if (reportUrl != null) {
                            Intent i = new Intent(reportNameTv.getContext(), WebviewActivity.class);
                            i.putExtra(CONST.WEB_URL, reportUrl);
                            reportNameTv.getContext().startActivity(i);
                        }
                    }
                });
            }
        });

        reportNameTv.setText(name);
        @DrawableRes int drawable = 0;
        if (StringUtil.isEmpty(reportBatch)) {
            //频度：0-日；1-周；2-旬；3-月；4-季；5-半年；6-年
            switch (frequency) {
                case 0: {
                    drawable = R.drawable.com_formlabel_icon_day;
                    break;
                }
                case 2: {
                    drawable = R.drawable.com_formlabel_icon_xun;
                    break;
                }
                case 3: {
                    drawable = R.drawable.com_formlabel_icon_month;
                    break;
                }
                //季报
                case 4: {
                    drawable = R.drawable.com_formlabel_icon_season;
                    break;
                }
                case 5: {
                    //半年
                    drawable = R.drawable.com_formlabel_icon_halfyear;
                    break;
                }
                case 6: {
                    //年报
                    drawable = R.drawable.com_formlabel_icon_year;
                    break;
                }
            }
        } else if (reportBatch.equals("0")) {
            //频度：0-日；1-周；2-旬；3-月；4-季；5-半年；6-年
            switch (frequency) {
                case 0: {
                    drawable = R.drawable.com_formlabel_icon_day;
                    break;
                }
                case 2: {
                    drawable = R.drawable.com_formlabel_icon_xun;
                    break;
                }
                case 3: {
                    drawable = R.drawable.com_formlabel_icon_month0;
                    break;
                }
                //季报
                case 4: {
                    break;
                }
                case 5: {
                    //半年
                    drawable = R.drawable.com_formlabel_icon_halfyear;
                    break;
                }
                case 6: {
                    //年报
                    drawable = R.drawable.com_formlabel_icon_year;
                    break;
                }
            }
        } else if (reportBatch.equals("1")) {
            //频度：0-日；1-周；2-旬；3-月；4-季；5-半年；6-年
            switch (frequency) {
                case 0: {
                    drawable = R.drawable.com_formlabel_icon_day;
                    break;
                }
                case 2: {
                    drawable = R.drawable.com_formlabel_icon_xun;
                    break;
                }
                case 3: {
                    drawable = R.drawable.com_formlabel_icon_month1;
                    break;
                }
                //季报
                case 4: {
                    drawable = R.drawable.com_formlabel_icon_season1;
                    break;
                }
                case 5: {
                    //半年
                    drawable = R.drawable.com_formlabel_icon_halfyear;
                    break;
                }
                case 6: {
                    //年报
                    drawable = R.drawable.com_formlabel_icon_year;
                    break;
                }
            }

        } else if (reportBatch.equals("2")) {
            //频度：0-日；1-周；2-旬；3-月；4-季；5-半年；6-年
            switch (frequency) {
                case 0: {
                    drawable = R.drawable.com_formlabel_icon_day;
                    break;
                }
                case 2: {
                    drawable = R.drawable.com_formlabel_icon_xun;
                    break;
                }
                case 3: {
                    drawable = R.drawable.com_formlabel_icon_month2;
                    break;
                }
                //季报
                case 4: {
                    drawable = R.drawable.com_formlabel_icon_season2;
                    break;
                }
                case 5: {
                    //半年
                    drawable = R.drawable.com_formlabel_icon_halfyear;
                    break;
                }
                case 6: {
                    //年报
                    drawable = R.drawable.com_formlabel_icon_year;
                    break;
                }
            }
        } else if (reportBatch.equals("3")) {
            //频度：0-日；1-周；2-旬；3-月；4-季；5-半年；6-年
            switch (frequency) {
                case 0: {
                    drawable = R.drawable.com_formlabel_icon_day;
                    break;
                }
                case 2: {
                    drawable = R.drawable.com_formlabel_icon_xun;
                    break;
                }
                case 3: {
                    drawable = R.drawable.com_formlabel_icon_month3;
                    break;
                }
                //季报
                case 4: {
                    drawable = R.drawable.com_formlabel_icon_season3;
                    break;
                }
                case 5: {
                    //半年
                    drawable = R.drawable.com_formlabel_icon_halfyear;
                    break;
                }
                case 6: {
                    //年报
                    drawable = R.drawable.com_formlabel_icon_year;
                    break;
                }
            }
        } else {
            drawable = R.drawable.com_formlabel_icon_monthother;
        }

        reportNameTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
    }

    public static void showPersonInviteStatus(View followButton, TextView followTv, int isInvite) {
        if (isInvite == 0) {
            followTv.setText("邀请");
            followTv.setTextColor(ZR.getColor(R.color.color_489dfff));
            followTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.com_btn_icon_plus, 0, 0, 0);
            followTv.setCompoundDrawablePadding((int) ZR.convertDpToPx(5));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border));
        } else if (isInvite == 1) {
            followTv.setText("已邀请");
            followTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            followTv.setTextColor(ZR.getColor(R.color.color_999999));
            followButton.setBackground(followButton.getResources().getDrawable(R.drawable.follow_border_gray));
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
                        String followStatus = response._data;
                        MyLog.d("follow Status: " + followStatus);
                        if (StringUtil.isEmpty(response._data)) {
                            followStatus = "0";
                        }

                        if (user.isFollow == 0) {
                            ZToast.toast("关注成功");
                        } else {
                            ZToast.toast("取消关注");

                        }
                        user.isFollow = Integer.valueOf(followStatus);
                        showPersonFollowStatus(followButton, followTv, user.isFollow);
                    }
                });
            }
        });
    }

    public static void setInviteClickListener(final AppUserResult user, final View followButton, final TextView followTv) {
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UserParams params = new UserParams();
                //follow user
                params.phone = user.phone;

                APIClient.inviteContact(params, new ZCallBack<ResponseModel<String>>() {
                    @Override
                    public void callBack(ResponseModel<String> response) {
                        user.isInvited = 1;
                        ZToast.toast("邀请成功成功");
                        showPersonInviteStatus(followButton, followTv, 1);
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

    /**
     * go to question detail activity
     *
     * @param v
     * @param requestId
     */
    public static void setRequestClickListener(View v, final String requestId) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyApplication.mCurrentActivity, QuestionActivity.class);
                i.putExtra(CONST.QUESTION_ID, requestId);
                ZGoto.to(i);
            }
        });
    }

    /**
     * go to answer detail activity
     *
     * @param v
     * @param answerId
     */
    public static void setAnswerClickListener(View v, final String answerId) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyApplication.mCurrentActivity, AnswerActivity.class);
                i.putExtra(CONST.ANSWER_ID, answerId);
                ZGoto.to(i);
            }
        });
    }

    public static void hide(View v) {
        if (v != null) {
            v.setVisibility(View.GONE);
        }
    }

    public static void show(View v) {
        if (v != null) {
            v.setVisibility(View.VISIBLE);
        }
    }

    public static boolean isValidEmailSuffix(String email) {
        if (email == null) return false;
        String emailSuffix = email.substring(email.indexOf("@"));
        int size = emailSufixs.length;
        for (int i = 0; i < size; i++) {
            if (emailSufixs[i].equals(emailSuffix)) {
                return true;
            }
        }
        return false;
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
