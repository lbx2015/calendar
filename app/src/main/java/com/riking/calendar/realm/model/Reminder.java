package com.riking.calendar.realm.model;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class Reminder extends RealmObject {
    //The title of the reminder
    public String title;
    //the time of the reminder
    public Date time;
}
