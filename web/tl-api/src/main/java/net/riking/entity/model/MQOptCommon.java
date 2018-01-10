package net.riking.entity.model;

import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

/**
 * mq操作公共类
 * @author jc.tan 2017年12月23日
 * @see
 * @since 1.0
 */
public class MQOptCommon extends BaseEntity {

	@Comment("操作人主键  ")
	private String userId;
	
	// 话题下面问题Id
	private String tqId;

	// (目标对象ID)
	private String attentObjId;

	// 1-点赞；2-收藏
	private Integer optType;

	// 1-问题；2-话题
	private Integer objType;

	// 0-取消；1-赞同/收藏
	private Integer enabled;

	// 问题回答ID
	private String questAnswerId;

	// 资讯信息id
	private String newsId;

	// 目标对象id
	private String objId;

	// 评论ID
	private String commentId;

	// 手机号
	private String phone;

	// 内容
	private String content;

	// 回复Id
	private String replyId;

	// 被回复用户id
	private String toUserId;

	@Transient
	// mq操作类型(消费者根据此类型判断mq操作)
	private Integer mqOptType;
	
	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getObjType() {
		return objType;
	}

	public void setObjType(Integer objType) {
		this.objType = objType;
	}

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getQuestAnswerId() {
		return questAnswerId;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setQuestAnswerId(String questAnswerId) {
		this.questAnswerId = questAnswerId;
	}

	public String getTqId() {
		return tqId;
	}

	public void setTqId(String tqId) {
		this.tqId = tqId;
	}

	public String getAttentObjId() {
		return attentObjId;
	}

	public void setAttentObjId(String attentObjId) {
		this.attentObjId = attentObjId;
	}

	public Integer getMqOptType() {
		return mqOptType;
	}

	public void setMqOptType(Integer mqOptType) {
		this.mqOptType = mqOptType;
	}

}
