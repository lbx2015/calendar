package net.riking.entity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;
import net.riking.entity.PageQuery;

/**
 * web
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("用户关注收藏管理")
public class UserFollowCollect extends PageQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2048938865811370457L;

	/** 序号 */
	private Integer serialNum;

	/** 关注人昵称/手机号 */
	private String userName;

	/** 操作人id */
	private String userId;

	/** 被关注人昵称/手机号 */
	private String toUserName;

	/** 被操作人id */
	private String toUserId;

	/** 被关注/收藏对象标题 */
	private String title;

	/** 操作对象 */
	private Integer optObject;

	/** 操作类型 */
	private Integer optType;

	/** 创建时间 */
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date createdTime;

	public Integer getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(Integer serialNum) {
		this.serialNum = serialNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getOptObject() {
		return optObject;
	}

	public void setOptObject(Integer optObject) {
		this.optObject = optObject;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
