package com.riking.calendar.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.lzy.ninegrid.NineGridView;
import com.riking.calendar.jiguang.Logger;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.NetStateReceiver;
import com.riking.calendar.util.ZPreference;

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
        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误

        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
// 请勿在“=”与appid之间添加任何空字符或者转义符
//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=59bf75be");

        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        // Setting.setShowLog(false);

        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity activity) {
                Logger.v(activity, "onActivityStopped");
            }

            @Override
            public void onActivityStarted(Activity activity) {
                mCurrentActivity = activity;
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
                Logger.v(activity, "onActivityPaused");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                //The activity would call onStop and onDestroy after another activity starts.
                if (mCurrentActivity == activity) {
                    mCurrentActivity = null;
                }
                Logger.v(activity, "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Logger.v(activity, "onActivityCreated");
            }
        });

        APP = this;
        preferences = MyApplication.APP.getSharedPreferences(CONST.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded();

        if (preferences.getBoolean(CONST.IS_LOGIN, false)) {
            Logger.d("zzw", "set user userId : " + preferences.getString(CONST.USER_ID, CONST.DEFAUT_REALM_DATABASE_NAME));
            builder.name(preferences.getString(CONST.USER_ID, CONST.DEFAUT_REALM_DATABASE_NAME));
        } else {
            builder.name(CONST.DEFAUT_REALM_DATABASE_NAME);
        }
        NetStateReceiver.registerNetworkStateReceiver(this);//初始化网络监听
        RealmConfiguration realmConfiguration = builder.build();
//        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration);

        //init hyphenate demo helper
//        DemoHelper.getInstance().init(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                NineGridView.setImageLoader(new GlidImageLoader());
            }
        }).start();
        ;

        /**
         * 初始化Mobile Analytics服务
         */

        // 获取MAN服务
        MANService manService = MANServiceProvider.getService();

        // 打开调试日志
        manService.getMANAnalytics().turnOnDebug();

        // MAN初始化方法之一，从AndroidManifest.xml中获取appKey和appSecret初始化
        manService.getMANAnalytics().init(this, getApplicationContext());

        // MAN另一初始化方法，手动指定appKey和appSecret
        // String appKey = "******";
        // String appSecret = "******";
        // manService.getMANAnalytics().init(this, getApplicationContext(), appKey, appSecret);

        // 若需要关闭 SDK 的自动异常捕获功能可进行如下操作,详见文档5.4
//        manService.getMANAnalytics().turnOffCrashReporter();

        // 通过此接口关闭页面自动打点功能，详见文档4.2
//        manService.getMANAnalytics().turnOffAutoPageTrack();

        // 设置渠道（用以标记该app的分发渠道名称），如果不关心可以不设置即不调用该接口，渠道设置将影响控制台【渠道分析】栏目的报表展现。如果文档3.3章节更能满足您渠道配置的需求，就不要调用此方法，按照3.3进行配置即可
        manService.getMANAnalytics().setChannel("Riking");

        // 若AndroidManifest.xml 中的 android:versionName 不能满足需求，可在此指定；
        // 若既没有设置AndroidManifest.xml 中的 android:versionName，也没有调用setAppVersion，appVersion则为null
//        manService.getMANAnalytics().setAppVersion("3.1.1");

        // 用户登录埋点
        manService.getMANAnalytics().updateUserAccount(ZPreference.getCurrentLoginUser().userName, ZPreference.getUserId());
    }

}
