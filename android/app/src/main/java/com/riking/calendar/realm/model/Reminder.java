package com.riking.calendar.realm.model;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class Reminder extends RealmObject {
    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    //The title of the reminder
    public String title;
    //the row of the reminder
    public Date time;
    public boolean isRepeat;
    public boolean isAllDay;
    public boolean isAccurate;
    public int aheadTime;
    //1,2,3,4,5,6,7,8,9
    public String repeatTime;
}
