package com.riking.calendar.realm.model;

import com.google.gson.annotations.SerializedName;
import com.riking.calendar.pojo.TaskModel;
import com.riking.calendar.pojo.params.Todo;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * Created by zw.zhang on 2017/7/12.
 */

public class Task extends RealmObject {
    @Ignore
    public static String IS_COMPLETE = "isComplete";
    @Ignore
    public static String DELETESTATE = "deleteState";
    @Ignore
    public static String TODO_ID = "todoId";
    @Ignore
    public static String COMPLETEDATE = "completeDate";
    @PrimaryKey
//    @Id
//    @Column(name = "todo_id", length = 17)
    public String todoId;

    // 用户Id
//    @Column(name = "user_id", length = 32)
    public String userId;

    // 提醒内容
//    @Column(name = "content", length = 255)
    @SerializedName("content")
    public String content;

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
    public String createdTime;

    // 待办提醒是否开启（0-否；1-是）
//    @Column(name = "is_complete", length = 1)
    public int isComplete;

    // 完成时间（yyyyMMddHHmm）
//    @Column(name = "complete_date", length = 12)
    public String completeDate;

    //    @Transient
    public int deleteState;
    public byte syncStatus;//同步的状态0:同步,1待同步
    //used to set alarm, the request code should not same.
    public int requestCode;

    public Task() {
    }

    public Task(TaskModel m) {
        todoId = m.todoId;
        userId = m.userId;
        content = m.content;
        isImportant = m.isImportant;
        isOpen = m.isOpen;
        strDate = m.strDate;
        createdTime = m.appCreatedTime;
        isComplete = m.isComplete;
        completeDate = m.completeDate;
    }

    public Todo getTodo() {
        Todo todo = new Todo();
        todo.todoId = todoId;
        todo.isImportant = isImportant;
        todo.content = content;
        todo.userId = userId;
        todo.isOpen = isOpen;
        todo.strDate = strDate;
        todo.createdTime = createdTime;
        todo.isCompleted = isComplete;
        todo.deleteFlag = deleteState;
        todo.completeDate = completeDate;
        return todo;
    }
}
