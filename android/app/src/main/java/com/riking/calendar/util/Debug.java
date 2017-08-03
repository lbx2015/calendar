package com.riking.calendar.util;

import android.os.Build;

import com.riking.calendar.BuildConfig;

/**
 * Created by zw.zhang on 2017/8/3.
 */

public class Debug {

    public static void Handle(Exception e){
        if(BuildConfig.DEBUG){
            e.printStackTrace();
        }
    }
}
