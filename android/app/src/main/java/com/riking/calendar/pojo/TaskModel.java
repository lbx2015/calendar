package com.riking.calendar.pojo;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.riking.calendar.realm.model.Task;

/**
 * Created by zw.zhang on 2017/8/7.
 */

public class TaskModel {
    public String todoId;

    // 用户Id
//    @Column(name = "user_id", length = 32)
    public String userId;

    // 提醒内容
//    @Column(name = "content", length = 255)
    @SerializedName("content")
    public String title;

    // 是否重要（0-不重要；1-重要）
//    @Column(name = "is_important", length = 1)
    public int isImportant;

    // 待办提醒是否开启（0-否；1-是）
//    @Column(name = "is_open", length = 1)
    public int isOpen;

    // 日期：yyyyMMddHHmm
//    @Column(name = "str_date", length = 12)
    public String strDate;

    // 手机端提供创建时间（yyyy-MM-dd HHmm）
//    @Column(name = "app_created_time", length = 12)
    public String appCreatedTime;

    // 待办提醒是否开启（0-否；1-是）
//    @Column(name = "is_complete", length = 1)
    public int isComplete;

    // 完成时间（yyyyMMddHHmm）
//    @Column(name = "complete_date", length = 12)
    public String completeDate;

    //    @Transient
    public int deleteState;

    public TaskModel(Task task) {
        this.todoId = task.todoId;
        this.userId = task.userId;
        this.title = task.title;
        this.isImportant = task.isImportant;
        this.isOpen = task.isOpen;
        this.strDate = task.strDate;
        this.appCreatedTime = task.appCreatedTime;
        this.isComplete = task.isComplete;
        this.completeDate = task.completeDate;
        this.deleteState = task.deleteState;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
