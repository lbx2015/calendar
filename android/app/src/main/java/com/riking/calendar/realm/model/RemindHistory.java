package com.riking.calendar.realm.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class RemindHistory extends RealmObject {
    @PrimaryKey
    @SerializedName("todo_id")
    public String id = UUID.randomUUID().toString();
    public String user_id;
    //The title of the reminder
    @NonNull
    @SerializedName("content")
    public String title;
    //the day of the reminder(yyyyMMdd)
    @SerializedName("star_date")
    public String day;
    //the reminderTimeCalendar of the reminder(HHmm)
    @SerializedName("start_time")
    public String time;
    //0 no remind, 1 remind
    public byte isRemind = 1;
    public byte client_type = 2;

}
