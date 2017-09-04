package com.riking.calendar.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.ldf.calendar.Const;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by zw.zhang on 2017/7/4.
 */

public class MyApplication extends Application {
    public static Context APP;
    public static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        APP = getApplicationContext();
        preferences = MyApplication.APP.getSharedPreferences(Const.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded();

        if (preferences.getBoolean(Const.IS_LOGIN, false)) {
            builder.name("rkdb");
        } else {
            builder.name(preferences.getString(Const.USER_ID, "rkdb"));
        }

        RealmConfiguration realmConfiguration = builder.build();
//        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
