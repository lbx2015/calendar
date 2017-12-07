package com.riking.calendar.util;

import android.widget.Toast;

import com.riking.calendar.app.MyApplication;

/**
 * Created by zw.zhang on 2017/12/7.
 */

public class ZToast {
    public static void toast(String content) {
        Toast.makeText(MyApplication.APP, content, Toast.LENGTH_SHORT).show();
    }
}
