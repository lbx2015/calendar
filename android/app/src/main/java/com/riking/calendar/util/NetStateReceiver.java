package com.riking.calendar.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.riking.calendar.jiguang.Logger;

import java.util.ArrayList;


/**
 * Created by zw.zhang on 2017/9/6.
 */

public class NetStateReceiver extends BroadcastReceiver {

    public static final IntentFilter FILTER = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

    private static boolean isNetAvailable = false;
    private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList<NetChangeObserver>();
    private static BroadcastReceiver mBroadcastReceiver;

    private static BroadcastReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (NetStateReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    /**
     * 添加网络监听
     *
     * @param observer
     */
    public static void registerObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        mNetChangeObservers.add(observer);
    }

    /**
     * 移除网络监听
     *
     * @param observer
     */
    public static void removeRegisterObserver(NetChangeObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }

    /**
     * 注册
     *
     * @param mContext
     */
    public static void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.setPriority(1000);
        mContext.registerReceiver(getReceiver(), filter);
    }

    /**
     * 清除
     *
     * @param mContext
     */
    public static void checkNetworkState(Context mContext) {
        Intent intent = new Intent();
        intent.setAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.sendBroadcast(intent);
    }

    /**
     * 反注册
     *
     * @param mContext
     */
    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (mBroadcastReceiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {

            }
        }

    }

    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetChangeObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetAvailable) {
                        observer.onNetConnected();
                    } else {
                        observer.onNetDisConnect();
                    }
                }
            }
        }
    }

    @Override
    public final void onReceive(Context context, Intent intent) {
        Logger.d("zzw", "onReciver " + intent);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            isNetAvailable = true;
        } else {
            isNetAvailable = false;
        }
        notifyObserver();
    }


    // 接口：
    public static interface NetChangeObserver {
        /**
         * 网络连接回调 type为网络类型
         */
        void onNetConnected();

        /**
         * 没有网络
         */
        void onNetDisConnect();
    }
}