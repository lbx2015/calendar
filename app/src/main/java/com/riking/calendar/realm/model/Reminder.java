package com.riking.calendar.realm.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class Reminder extends RealmObject {
    //The title of the reminder
    public String title;
    //the row of the reminder
    public Date time;
    //This is just a flag to determine whether this event is import
    public boolean isImport;
}
