package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.entity.PageQuery;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("代办表")
@Entity
@Table(name = "t_todo")
public class Todo extends PageQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3194063320181067468L;

	/**
	 * 代办事项实体类
	 * 
	 * @author Lucky.Liu on 2017/8/05.
	 */

	@Id
	@Comment("pk 手机端时间戳：yyyyMMddHHmmssSSS")
	@Column(name = "todo_id", length = 17)
	private String todoId;

	@Comment("用户Id")
	@Column(name = "user_id", length = 32)
	private String userId;

	@Comment("内容")
	@Column(name = "content", length = 255)
	private String content;

	@Comment("是否重要（0-不重要；1-重要）")
	@Column(name = "is_important", length = 1)
	private Integer isImportant;

	@Comment("待办提醒是否开启（0-否；1-是）")
	@Column(name = "is_open", length = 1)
	private Integer isOpen;

	@Comment("日期：yyyyMMddHHmm")
	@Column(name = "str_date", length = 12)
	private String strDate;

	@Comment("手机端提供创建时间（yyyyMMddHHmm）")
	@Column(name = "app_created_time", length = 12)
	private String appCreatedTime;

	@Comment("是否完成（0-未完成；1-已完成）")
	@Column(name = "is_complete", length = 1)
	private Integer isComplete;

	@Comment("客户端数据来源：1-IOS;2-Android;3-其它")
	@Column(name = "client_type", length = 1)
	private Integer clientType;

	@Comment("完成时间yyyyMMddHHmm")
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

	public String getTodoId() {
		return todoId;
	}

	public void setTodoId(String todoId) {
		this.todoId = todoId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public void setIsImportant(Integer isImportant) {
		this.isImportant = isImportant;
	}

	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
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
		return "Todo [todoId=" + todoId + ", userId=" + userId + ", content=" + content + ", isImportant=" + isImportant
				+ ", isOpen=" + isOpen + ", strDate=" + strDate + ", appCreatedTime=" + appCreatedTime + ", isComplete="
				+ isComplete + ", completeDate=" + completeDate + ", deleteState=" + deleteState + "]";
	}

}
