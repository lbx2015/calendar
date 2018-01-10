package com.riking.calendar.realm.model;

import com.riking.calendar.pojo.ReminderModel;
import com.riking.calendar.util.DateUtil;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class Reminder extends RealmObject {
    @Ignore
    public static String REMINDER_ID = "id";
    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    public String userId;
    //The title of the reminder
    public String title;
    //the day of the reminder(yyyyMMdd)
    public String day;
    //the reminderTimeCalendar of the reminder(HHmm)
    public String time;
    public Date reminderTime;
    //o false 1 yes
    public byte isAllDay;
    public int aheadTime;
    public int endTime;
    //1...7
    public String repeatWeek;
    //0(not repeat),1(work day),2(holiday),3(week repeat)
    public byte repeatFlag;
    //0-6
    public byte currentWeek;
    //0 not delete, 1 delete
    public byte deleteState;
    //0 no remind, 1 remind
    public byte isRemind = 1;
    public byte clientType = 2;
    public byte syncStatus;//同步的状态0:同步,1待同步
    //used to set alarm, the request code should not same.
    public int requestCode;

    //    @Comment("报送开始时间（yyyyMMddHHmm）")
//    @Column(name = "submit_start_time", length = 8)
    public String submitStartTime;

    //    @Comment("报送截止时间（yyyyMMddHHmm）")
//    @Column(name = "submit_end_time", length = 8)
    public String submitEndTime;
    //    @Comment("报表id")
//    @Column(name = "report_id", length = 32)
    public String reportId;

    public Reminder() {
    }

    public Reminder(ReminderModel m) {
        id = m.id;
        userId = m.userId;
        title = m.title;
        day = m.day;
        time = m.time.replace("[^\\d]", "");
        reminderTime = DateUtil.get(day, time);
        isAllDay = m.isAllDay;
        aheadTime = m.aheadTime;
        endTime = m.endTime;
        repeatWeek = m.repeatWeek;
        repeatFlag = m.repeatFlag;
        currentWeek = m.currentWeek;
        deleteState = m.deleteState;
        isRemind = m.isRemind;
        clientType = m.clientType;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", reminderTime=" + reminderTime +
                ", isAllDay=" + isAllDay +
                ", aheadTime=" + aheadTime +
                ", endTime=" + endTime +
                ", repeatWeek='" + repeatWeek + '\'' +
                ", repeatFlag=" + repeatFlag +
                ", currentWeek=" + currentWeek +
                ", deleteState=" + deleteState +
                ", isRemind=" + isRemind +
                ", clientType=" + clientType +
                ", syncStatus=" + syncStatus +
                ", requestCode=" + requestCode +
                ", submitStartTime='" + submitStartTime + '\'' +
                ", submitEndTime='" + submitEndTime + '\'' +
                ", reportId='" + reportId + '\'' +
                '}';
    }
}
