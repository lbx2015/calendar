package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("问题回答收藏信息 表")
@Entity
@Table(name = "t_q_answer_collect_info")
public class QAnswerCollectInfo extends BaseProp {

	private static final long serialVersionUID = 7996371062026840698L;
	
	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Comment("目标对象主键")
	@Column(name = "question_answer_id", nullable = false)
	private String questionAnswerId;

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

		
}
