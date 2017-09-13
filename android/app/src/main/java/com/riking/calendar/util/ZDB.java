package com.riking.calendar.util;

import com.riking.calendar.realm.model.Reminder;
import com.riking.calendar.retrofit.APIClient;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by zw.zhang on 2017/7/18.
 */

public class ZDB {
    public static final ZDB Instance = new ZDB();

    public void cancelAlarmsWhenLoginOut() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Reminder> reminders = realm.where(Reminder.class).findAll();
        if (reminders != null) {
            for (Reminder r : reminders) {
                APIClient.cancelReminds(r.requestCode);
            }
        }
    }
}
