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
    public String userId;
    //The title of the reminder
    public String title;
    //the day of the reminder(yyyyMMdd)
    public String day;
    //the time of the reminder(HHmm)
    public String time;
    public Date reminderTime;
    //o false 1 yes
    public byte isAllDay;
    public int aheadTime;
    public int endTime;
    //1...7
    public String repeatWeek;
    //0,1,2,3
    public byte repeatFlag;
    //0-6
    public byte currentWeek;
    public byte deleteState;
    //0 no remind, 1 remind
    public byte isRemind = 1;
    public byte clientType = 2;
}
