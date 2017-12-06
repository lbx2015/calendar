package com.riking.calendar.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.necer.ncalendar.utils.MyLog;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.ZGoto;
import com.riking.calendar.util.ZPreference;

/**
 * With Login check and not jump activity
 * Created by zw.zhang on 2017/12/5.
 */

public abstract class ZClickListenerWithLoginCheck implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        if (ZPreference.pref.getBoolean(CONST.IS_LOGIN, false)) {
            click(v);
        } else {
            ZGoto.toLoginActivity();
        }
    }

    public abstract void click(View v);
}
