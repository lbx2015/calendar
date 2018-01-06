package net.riking.entity.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.entity.BaseEntity;

public class SysNoticeResult extends BaseEntity {
	// 消息通知id
	private String noticeId;

	// 消息通知标题
	private String title;

	// 来自某用户对象的用户昵称
	private String fromUserName;

	// 用户头像
	private String userPhotoUrl;

	// 根据依据dataType存储对象id
	private String objId;

	// 消息内容
	private String content;

	// 数据类型0-系统信息；1-被邀请回答的邀请；2-问题回答被点赞或收藏；3-问题被关注；4-被关注的用户；5-评论被点赞；6-问题回答的被评论；7-评论的回复和回复的被回复
	private Integer dataType;

	// 是否已读：0-未读; 1-已读
	private Integer isRead;

//	// 翻页时间戳
//	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
//	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
//	private Date reqTimeStamp;

	// 创建时间
	@DateTimeFormat(pattern = "yyyyMMddHHmmssSSS")
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date createdTime;

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	public String getUserPhotoUrl() {
		return userPhotoUrl;
	}

	public void setUserPhotoUrl(String userPhotoUrl) {
		this.userPhotoUrl = userPhotoUrl;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

//	public Date getReqTimeStamp() {
//		return reqTimeStamp;
//	}
//
//	public void setReqTimeStamp(Date reqTimeStamp) {
//		this.reqTimeStamp = reqTimeStamp;
//	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

}
