package com.riking.calendar.realm.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class Task extends RealmObject {
    @PrimaryKey
    @SerializedName("todo_id")
    public String id = UUID.randomUUID().toString();
    //The title of the reminder
    @NonNull
    @SerializedName("content")
    public String title;
    //the row of the reminder
    public Date remindTime;
    //This is just a flag to determine whether this event is import
    public boolean isImport;
    public boolean isDone;
    //'2016-07-31'
    public Date completeDay;
    public Date createTime;
    public boolean isReminded;
}
