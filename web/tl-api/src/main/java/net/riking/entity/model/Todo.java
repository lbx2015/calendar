package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "t_todo")
public class Todo {
	@Id
	@Column(name = "todo_id", length = 17)
	private String todo_Id;

	// 用户Id
	@Column(name = "user_id", length = 32)
	private String userId;

	// 提醒内容
	@Column(name = "content", length = 255)
	private String content;

	// 是否重要（0-不重要；1-重要）
	@Column(name = "is_important", length = 1)
	private int isImportant;

	// 待办提醒是否开启（0-否；1-是）
	@Column(name = "is_open", length = 1)
	private int isOpen;

	// 日期：yyyyMMddHHmm
	@Column(name = "str_date", length = 12)
	private String strDate;

	// 手机端提供创建时间（yyyy-MM-dd HHmm）
	@Column(name = "app_created_time", length = 12)
	private String appCreatedTime;

	// 待办提醒是否开启（0-否；1-是）
	@Column(name = "is_complete", length = 1)
	private int isComplete;

	// 完成时间（yyyyMMddHHmm）
	@Column(name = "complete_date", length = 12)
	private String completeDate;
	
	@Transient
	private int deleteState;
	

	public int getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(int deleteState) {
		this.deleteState = deleteState;
	}

	public String getTodo_Id() {
		return todo_Id;
	}

	public void setTodo_Id(String todo_Id) {
		this.todo_Id = todo_Id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsImportant() {
		return isImportant;
	}

	public void setIsImportant(int isImportant) {
		this.isImportant = isImportant;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public String getAppCreatedTime() {
		return appCreatedTime;
	}

	public void setAppCreatedTime(String appCreatedTime) {
		this.appCreatedTime = appCreatedTime;
	}

	public int getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(int isComplete) {
		this.isComplete = isComplete;
	}

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	@Override
	public String toString() {
		return "Todo [todo_Id=" + todo_Id + ", userId=" + userId + ", content=" + content + ", isImportant="
				+ isImportant + ", isOpen=" + isOpen + ", strDate=" + strDate + ", appCreatedTime=" + appCreatedTime
				+ ", isComplete=" + isComplete + ", completeDate=" + completeDate + "]";
	}
	

}
