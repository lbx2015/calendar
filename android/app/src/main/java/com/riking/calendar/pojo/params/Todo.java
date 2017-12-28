package com.riking.calendar.pojo.params;

import com.riking.calendar.pojo.base.PageQuery;
import com.riking.calendar.realm.model.Task;

/**
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
//@Comment("代办表")
//@Entity
//@Table(name = "t_todo")
public class Todo extends PageQuery {

    //	@Id
//	@Comment("pk 手机端时间戳：yyyyMMddHHmmssSSS")
//	@Column(name = "todo_id", length = 17)
    public String todoId;

    //	@Comment("内容")
//	@Column(name = "content", length = 255)
    public String content;

    //	@Comment("是否重要（0-不重要；1-重要）")
//	@Column(name = "is_important", length = 1)
    public int isImportant;

    //	@Comment("待办提醒是否开启（0-否；1-是）")
//	@Column(name = "is_open", length = 1)
    public int isOpen;

    //	@Comment("日期：yyyyMMddHHmm")
//	@Column(name = "str_date", length = 12)
    public String strDate;

    //	@Comment("手机端提供创建时间（yyyyMMddHHmm）")
//	@Column(name = "app_created_time", length = 12)
    public String createdTime;

    //	@Comment("是否完成（0-未完成；1-已完成）")
//	@Column(name = "is_complete", length = 1)
    public int isCompleted;

    //	@Comment("客户端数据来源：1-IOS;2-Android;3-其它")
//	@Column(name = "client_type", length = 1)
    public int clientType = 2;

    //	@Comment("完成时间yyyyMMddHHmm")
//	@Column(name = "complete_date", length = 12)
    public String completeDate;

    //	@Transient 0不删除，1删除
    public int deleteFlag;

    public Todo() {
    }

    public Todo(Task t) {
        todoId = t.todoId;
        isImportant = t.isImportant;
        content = t.content;
        userId = t.userId;
        isOpen = t.isOpen;
        strDate = t.strDate;
        createdTime = t.createdTime;
        isCompleted = t.isComplete;
        deleteFlag = t.deleteState;
        completeDate = t.completeDate;
    }

    @Override
    public String toString() {
        return "Todo [todoId=" + todoId + ", userId=" + userId + ", content=" + content + ", isImportant=" + isImportant
                + ", isOpen=" + isOpen + ", strDate=" + strDate + ", appCreatedTime=" + createdTime + ", isComplete="
                + isCompleted + ", completeDate=" + completeDate + ", deleteState=" + deleteFlag + "]";
    }

    public Task getTask() {
        Task task = new Task();
        task.isComplete = isCompleted;
        task.userId = userId;
        task.isImportant = isImportant;
        task.completeDate = completeDate;
        task.createdTime = completeDate;
        task.content = content;
        task.isOpen = isOpen;
        task.deleteState = deleteFlag;
        task.strDate = strDate;
        task.todoId = todoId;
        return task;
    }
}
