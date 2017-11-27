package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseAuditProp;

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

	@Comment("操作人主键 : fk t_app_user 发表回复的user_id")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Comment("被操作人主键: fk t_app_user 被评论人ID")
	@Column(name = "to_user_id", nullable = false)
	private String toUserId;

	@Comment("目标对象评论主键: fk t_qa_comment")
	@Column(name = "comment_id", nullable = false)
	private String commentId;

	@Comment("目标对象评论回复主键: fk t_qac_reply 回复ID")
	@Column(name = "reply_id", nullable = false)
	private String reply_id;

	@Comment("内容")
	@Column(name = "content", nullable = false)
	private String content;

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

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}

	public String getReply_id() {
		return reply_id;
	}

	public void setReply_id(String reply_id) {
		this.reply_id = reply_id;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

}
