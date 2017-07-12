package com.riking.calendar.app;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by zw.zhang on 2017/7/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
    }
}
