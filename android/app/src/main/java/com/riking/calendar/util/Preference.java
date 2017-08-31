package com.riking.calendar.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.ldf.calendar.Const;
import com.riking.calendar.app.MyApplication;

/**
 * Created by zw.zhang on 2017/8/31.
 */

public class Preference {
    public static SharedPreferences pref = MyApplication.APP.getSharedPreferences(Const.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
}
