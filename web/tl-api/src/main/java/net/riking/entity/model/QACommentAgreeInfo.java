package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("问题回答评论点赞信息 表")
@Entity
@Table(name = "t_qa_comment_agree_info")
public class QACommentAgreeInfo  extends BaseProp{

	private static final long serialVersionUID = 2472259115183313232L;
	
	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Comment("目标对象主键")
	@Column(name = "qa_comment_id", nullable = false)
	private String qACommentId;

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
	
}
