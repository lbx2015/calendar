package com.riking.calendar.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ldf.calendar.Const;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.NetStateReceiver;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by zw.zhang on 2017/7/4.
 */

public class MyApplication extends Application {
    public static Context APP;
    public static SharedPreferences preferences;
    public static Activity mCurrentActivity = null;
    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                Logger.v(activity, "onActivityStopped");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                Logger.v(activity, "onActivityStarted");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Logger.v(activity, "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                Logger.v(activity, "onActivityResumed");
                mCurrentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                mCurrentActivity = null;
                Logger.v(activity, "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mCurrentActivity = null;
                Logger.v(activity, "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Logger.v(activity, "onActivityCreated");
            }
        });

        APP = getApplicationContext();
        preferences = MyApplication.APP.getSharedPreferences(Const.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded();

        if (preferences.getBoolean(Const.IS_LOGIN, false)) {
            Logger.d("zzw", "set user id : " + preferences.getString(Const.USER_ID, CONST.DEFAUT_REALM_DATABASE_NAME));
            builder.name(preferences.getString(Const.USER_ID, CONST.DEFAUT_REALM_DATABASE_NAME));
        } else {
            builder.name(CONST.DEFAUT_REALM_DATABASE_NAME);
        }
        NetStateReceiver.registerNetworkStateReceiver(this);//初始化网络监听
        RealmConfiguration realmConfiguration = builder.build();
//        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
