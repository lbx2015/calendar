package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseAuditProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("行业资讯的评论回复表")
@Entity
@Table(name = "t_nc_reply")
public class NCReply extends BaseAuditProp {

	@Comment("操作人主键: fk t_app_user 发表回复的user_id")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Comment("被操作人主键: fk t_app_user 被评论人ID")
	@Column(name = "to_user_id", nullable = false)
	private String toUserId;

	@Comment("目标对象评论主键: fk t_news_comment 行业资讯的评论表")
	@Column(name = "comment_id", nullable = false)
	private String commentId;

	@Comment("目标对象评论回复主键: fk t_nc_reply 回复ID")
	@Column(name = "reply_id")
	private String replyId;

	@Comment("内容")
	@Column(name = "content", nullable = false)
	private String content;

	@Transient
	private String userName;

	// 用户头像Url
	@Transient
	private String photoUrl;

	public NCReply(String id, Date createdTime, Date modifiedTime, String userId, String toUserId, String commentId,
			String replyId, String content, String userName, String photoUrl) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.userId = userId;
		this.toUserId = toUserId;
		this.commentId = commentId;
		this.replyId = replyId;
		this.content = content;
		this.userName = userName;
		this.photoUrl = photoUrl;
	}

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

}
