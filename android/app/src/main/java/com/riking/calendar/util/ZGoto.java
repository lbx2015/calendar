package com.riking.calendar.util;

import android.app.Activity;
import android.content.Intent;

import com.riking.calendar.activity.LoginNavigateActivity;
import com.riking.calendar.app.MyApplication;

/**
 * Created by zw.zhang on 2017/11/21.
 */

public class ZGoto {
    public static void toLoginActivity(Activity a) {
        a.startActivity(new Intent(a, LoginNavigateActivity.class));
    }

    public static <T extends Activity> void to(Activity a, Class<T> t) {
        a.startActivity(new Intent(a, t));
    }

    public static <T extends Activity> void to(Class<T> t) {
        Activity a = MyApplication.mCurrentActivity;
        a.startActivity(new Intent(a, t));
    }
}
