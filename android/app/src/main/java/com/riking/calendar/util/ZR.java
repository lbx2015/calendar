package com.riking.calendar.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.riking.calendar.R;
import com.riking.calendar.app.MyApplication;

import java.text.DecimalFormat;

/**
 * Created by zw.zhang on 2017/7/14.
 */

public class ZR {
    public static String jumpClass;

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
                .apply(options.fitCenter().placeholder(R.drawable.default_user_icon))
                .into(v);
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
