package com.riking.calendar.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;

import java.lang.ref.WeakReference;

/**
 * With Login check and not jump activity
 * Created by zw.zhang on 2017/12/5.
 */

public abstract class ZClickListenerWithLoginCheck implements View.OnClickListener {
    static WeakReference<ZClickListenerWithLoginCheck> weakRef;
    View v;

    @Override
    public void onClick(View v) {
        this.v = v;
        MyLog.d("onClick::" + v.getId());
        if (ZPreference.pref.getBoolean(CONST.IS_LOGIN, false)) {
            click(v);
        } else {
            weakRef = new WeakReference<ZClickListenerWithLoginCheck>(this);
            ZPreference.put(CONST.CHECK_NOT_LOGIN_ON_CLICK, true);
            ZGoto.toLoginActivity();
        }
    }

    public abstract void click(View v);


    public static class MyReceiver extends BroadcastReceiver {

        private static final String TAG = "MyReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            Log.i(TAG, msg);
            if (weakRef != null) {
                ZClickListenerWithLoginCheck z = weakRef.get();
                if (z != null) {
                    weakRef = null;
                    z.click(z.v);
                }
            }
        }
    }
}
