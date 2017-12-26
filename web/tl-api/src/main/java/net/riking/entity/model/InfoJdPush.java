package net.riking.entity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

/**
 * 消息极光推送
 * @author jc.tan 2017年12月26日
 * @see
 * @since 1.0
 */
public class InfoJdPush extends BaseEntity {

	@Comment("操作人主键  ")
	private String userId;

	@Comment("用户名")
	private String userName;

	// 用户头像
	private String photoUrl;

	// 操作类型 1-评论 2-点赞 3-邀请 4-关注 5-回答
	private Integer optType;

	// 回答的内容
	private String answerContent;

	// 创建时间
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date createdTime;

	// 问题id
	private String tqId;

	// 问题回答id
	private String questAnswerId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getTqId() {
		return tqId;
	}

	public void setTqId(String tqId) {
		this.tqId = tqId;
	}

	public String getQuestAnswerId() {
		return questAnswerId;
	}

	public void setQuestAnswerId(String questAnswerId) {
		this.questAnswerId = questAnswerId;
	}

}
