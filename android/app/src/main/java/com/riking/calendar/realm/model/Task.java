package com.riking.calendar.realm.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class Task extends RealmObject {
    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    //The title of the reminder
    @NonNull
    public String title;
    //the row of the reminder
    public Date remindTime;
    //This is just a flag to determine whether this event is import
    public boolean isImport;
    public boolean isDone;
    //'2016-07-31'
    public Date completeDay;
}
