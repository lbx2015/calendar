package com.riking.calendar.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.riking.calendar.app.MyApplication;
import com.riking.calendar.pojo.resp.AppUserResp;

import java.util.Set;

/**
 * Created by zw.zhang on 2017/8/31.
 */

public class ZPreference {
    public static SharedPreferences pref = MyApplication.APP.getSharedPreferences(CONST.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);

    public static String getUserId() {
//        return  "15a77e10e2f44ea28da7a94e2e5320c2";
        return ZPreference.pref.getString(CONST.USER_ID, "");
    }

    public static boolean isLogin() {
        return ZPreference.pref.getBoolean(CONST.IS_LOGIN, false);
    }

    public static void remove(String key) {
        SharedPreferences.Editor e = pref.edit();
        e.remove(key);
        e.commit();
    }

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

    public static void saveUserInfoAfterLogin(AppUserResp u) {
        SharedPreferences.Editor e = pref.edit();
        e.putBoolean(CONST.IS_LOGIN, true);
        e.putString(CONST.USER_ID, u.userId);
        e.putString(CONST.USER_NAME, u.userName);
        e.putString(CONST.USER_EMAIL, u.email);
        e.putString(CONST.PHONE_NUMBER, u.phone);
        e.putInt(CONST.USER_SEX,u.sex);
        e.putString(CONST.USER_REAL_NAME, u.realName);
        e.putString(CONST.USER_IMAGE_URL, u.photoUrl);
        e.putString(CONST.USER_EMAIL, u.email);
        Gson s = new Gson();
        e.putString(CONST.CURRENT_LOGIN_USER, s.toJson(u));
        e.commit();
    }

    public static AppUserResp getCurrentLoginUser() {
        Gson s = new Gson();
        return s.fromJson(pref.getString(CONST.CURRENT_LOGIN_USER, ""), AppUserResp.class);
    }
}
