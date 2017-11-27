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
@Comment("问题回答的评论 表")
@Entity
@Table(name = "t_qa_comment")
public class QAComment extends BaseAuditProp {

	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Comment("目标对象主键")
	@Column(name = "question_answer_id", nullable = false)
	private String questionAnswerId;

	@Comment("内容")
	@Column(name = "content", nullable = false)
	private String content;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQuestionAnswerId() {
		return questionAnswerId;
	}

	public void setQuestionAnswerId(String questionAnswerId) {
		this.questionAnswerId = questionAnswerId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
