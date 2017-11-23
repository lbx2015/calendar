package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("问题回答评论回复信息 表")
@Entity
@Table(name = "t_qa_comment_reply_info")
public class QACommentReplyInfo extends BaseProp{
	
	private static final long serialVersionUID = 5567244505452215683L;

	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Comment("被操作人主键  ")
	@Column(name = "to_user_id", nullable = false)
	private String toUserId;
	
	@Comment("目标对象评论主键")
	@Column(name = "qa_comment_id", nullable = false)
	private String qACommentId;
	
	@Comment("目标对象评论回复主键")
	@Column(name = "qa_comment_reply_id", nullable = false)
	private String qACommentReplyId;
	
	@Comment("内容")
	@Column(name = "content", nullable = false)
	private String content;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getqACommentId() {
		return qACommentId;
	}

	public void setqACommentId(String qACommentId) {
		this.qACommentId = qACommentId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getqACommentReplyId() {
		return qACommentReplyId;
	}

	public void setqACommentReplyId(String qACommentReplyId) {
		this.qACommentReplyId = qACommentReplyId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	
}
