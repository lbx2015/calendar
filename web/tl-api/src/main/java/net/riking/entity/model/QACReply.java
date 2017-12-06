package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseAuditProp;
import net.riking.entity.resp.FromUser;
import net.riking.entity.resp.ToUser;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("问题回答评论回复信息 表")
@Entity
@Table(name = "t_qac_reply")
public class QACReply extends BaseAuditProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3040318861369712268L;

	@Comment("操作人主键 : fk t_app_user 发表回复的user_id")
	@Column(name = "from_user_id", nullable = false)
	private String fromUserId;

	@Comment("被操作人主键: fk t_app_user 被评论人ID")
	@Column(name = "to_user_id")
	private String toUserId;

	@Comment("目标对象评论主键: fk t_qa_comment")
	@Column(name = "comment_id", nullable = false)
	private String commentId;

	@Comment("目标对象评论回复主键: fk t_qac_reply 回复ID")
	@Column(name = "reply_id")
	private String replyId;

	@Comment("内容")
	@Column(name = "content", nullable = false)
	private String content;

	@Transient
	FromUser fromUser;

	@Transient
	ToUser toUser;

	// 发表回复的名称
	@Transient
	private String fromUserName;

	// 被评论人名称
	@Transient
	private String toUserName;

	public QACReply() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QACReply(String id, Date createdTime, Integer isAudit, String userId, String toUserId, String commentId,
			String replyId, String content, String fromUserName, String toUserName) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setIsAudit(isAudit);
		this.fromUserId = userId;
		this.toUserId = toUserId;
		this.commentId = commentId;
		this.replyId = replyId;
		this.content = content;
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getContent() {
		return content;
	}

	public FromUser getFromUser() {
		return fromUser;
	}

	public void setFromUser(FromUser fromUser) {
		this.fromUser = fromUser;
	}

	public ToUser getToUser() {
		return toUser;
	}

	public void setToUser(ToUser toUser) {
		this.toUser = toUser;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
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

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

}
