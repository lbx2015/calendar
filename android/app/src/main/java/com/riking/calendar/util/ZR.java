package com.riking.calendar.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * Created by zw.zhang on 2017/7/14.
 */

public class ZR {
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