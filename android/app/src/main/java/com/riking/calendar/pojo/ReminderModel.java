package com.riking.calendar.pojo;

import com.google.gson.annotations.SerializedName;
import com.riking.calendar.gson.ExcludeGsonField;
import com.riking.calendar.realm.model.Reminder;

import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/8/7.
 */

public class ReminderModel {
    @PrimaryKey
    @SerializedName("reminderId")
    public String id;
    public String userId;
    //The title of the reminder
    @SerializedName("content")
    public String title;
    //the day of the reminder(yyyyMMdd)
    @SerializedName("strDate")
    public String day;
    //the reminderTimeCalendar of the reminder(HHmm)
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

    @ExcludeGsonField
    public byte operationType;

    public ReminderModel() {
    }

    public ReminderModel(Reminder r) {
        id = r.id;
        userId = r.userId;
        title = r.title;
        day = r.day;
        time = r.time.replace("[^\\d]","");
        repeatFlag = r.repeatFlag;
        isAllDay = r.isAllDay;
        aheadTime = r.aheadTime;
        endTime = r.endTime;
        repeatWeek = r.repeatWeek;
        currentWeek = r.currentWeek;
        deleteState = r.deleteState;
        isRemind = r.isRemind;
    }

    @Override
    public String toString() {
        return "ReminderModel{" +
                "userId='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", repeatFlag=" + repeatFlag +
                ", isAllDay=" + isAllDay +
                ", aheadTime=" + aheadTime +
                ", endTime=" + endTime +
                ", repeatWeek='" + repeatWeek + '\'' +
                ", currentWeek=" + currentWeek +
                ", deleteState=" + deleteState +
                ", isRemind=" + isRemind +
                ", clientType=" + clientType +
                '}';
    }
}
