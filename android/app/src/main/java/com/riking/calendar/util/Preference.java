package com.riking.calendar.util;

import android.content.Context;
import android.content.SharedPreferences;


import com.riking.calendar.app.MyApplication;

import java.util.Set;

/**
 * Created by zw.zhang on 2017/8/31.
 */

public class Preference {
    public static SharedPreferences pref = MyApplication.APP.getSharedPreferences(CONST.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

    public static <T> void put(String key, T val) {
        SharedPreferences.Editor editor = pref.edit();
        if (val instanceof String) {
            editor.putString(key, (String) val);
        } else if (val instanceof Boolean) {
            editor.putBoolean(key, (Boolean) val);
        } else if (val instanceof Integer) {
            editor.putInt(key, (Integer) val);
        } else if (val instanceof Float) {
            editor.putFloat(key, (Float) val);
        } else if (val instanceof Long) {
            editor.putLong(key, (Long) val);
        } else if (val instanceof Set) {
            editor.putStringSet(key, (Set<String>) val);
        }
        editor.commit();
    }
}
