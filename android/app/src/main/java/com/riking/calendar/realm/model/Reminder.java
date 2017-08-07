package com.riking.calendar.realm.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class Reminder extends RealmObject {
    @PrimaryKey
    @SerializedName("reminderId")
    public String id = UUID.randomUUID().toString();
    public String userId;
    //The title of the reminder
    @SerializedName("content")
    public String title;
    //the day of the reminder(yyyyMMdd)
    @SerializedName("strDate")
    public String day;
    //the time of the reminder(HHmm)
    @SerializedName("startTime")
    public String time;
    //0,1,2,3
    @SerializedName("repeatFlag")
    public byte repeatFlag;
    //o false 1 yes
    @SerializedName("isAllday")
    public byte isAllDay;
    @SerializedName("beforeTime")
    public int aheadTime;
    public int endTime;
    //1...7
    @SerializedName("repeatValue")
    public String repeatWeek;
    //0-6
    @SerializedName("currWeek")
    public byte currentWeek;
    public byte deleteState;
    //0 no remind, 1 remind
    public byte isRemind = 1;
    public byte clientType = 2;
}
