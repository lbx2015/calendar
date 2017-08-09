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
    public String user_id;
    //The title of the reminder
    @NonNull
    @SerializedName("content")
    public String title;
    //the row of the reminder
    @SerializedName("str_date")
    public Date remindTime;
    //This is just a flag to determine whether this event is import
    @SerializedName("is_important")
    public byte isImport;
    @SerializedName("is_complete")
    public byte isDone;
    @SerializedName("is_open")
    public byte isReminded;
    //'2016-07-31'
    @SerializedName("complete_date")
    public Date completeDay;
    @SerializedName("created_date")
    public Date createTime;
    public byte client_type = 2;

}
