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
    public String id = UUID.randomUUID().toString();
    public String user_id;
    //The title of the reminder
    @SerializedName("content")
    public String title;
    //the day of the reminder(yyyyMMdd)
    @SerializedName("star_date")
    public String day;
    //the time of the reminder(HHmm)
    @SerializedName("start_time")
    public String time;
    //0,1,2,3
    @SerializedName("repeat_flag")
    public byte repeatFlag;
    //o false 1 yes
    @SerializedName("is_allday")
    public byte isAllDay;
    @SerializedName("before_time")
    public int aheadTime;
    //1...7
    @SerializedName("repeat_value")
    public String repeatWeek;
    //0-6
    @SerializedName("curr_week")
    public byte currentWeek;

    @SerializedName("is_remind")
    //0 no remind, 1 remind
    public byte isRemind = 1;
    public byte client_type = 2;
}
