package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("屏蔽问题信息 表")
@Entity
@Table(name = "t_quest_ignore_info")
public class QuestionIgnore extends BaseProp{

	private static final long serialVersionUID = 6271454792517216994L;

	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Comment("目标对象主键")
	@Column(name = "question_id", nullable = false)
	private String questionId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	
	
	
	
}
