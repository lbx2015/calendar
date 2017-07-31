package com.riking.calendar.realm.model;

import android.support.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class Task extends RealmObject {
    @PrimaryKey
    public String id = UUID.randomUUID().toString();
    //The title of the reminder
    public String title;
    //the row of the reminder
    public Date time;
    //This is just a flag to determine whether this event is import
    public boolean isImport;
    public boolean isDone;
}
