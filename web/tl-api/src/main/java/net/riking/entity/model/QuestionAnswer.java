package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;

/**
 * 
 * @author tao.yuan
 * @version crateTime：2017年11月17日 下午1:44:51
 * @used 问题回答表
 */
@Entity
@Table(name = "t_question_answer")
public class QuestionAnswer {

	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Comment("回答的问题id")
	@Column(name = "question_id", length = 32)
	private String questionId;//回答的问题id
	
	@Comment("回答这个问题的用户id")
	@Column(name = "user_id", length = 32)
	private String userId;//回答这个问题的用户id
	
	@Comment("回答这个问题的答案")
	@Column(name = "answer_content", length = 512)
	private String answerContent;//回答这个问题的答案
	
	@Comment("回答这个问题的时间")
	@Column(name = "create_time", length = 16)
	private String createTime;//回答这个问题的时间
	
	@Comment("删除状态")
	@Column(name = "delete_state", length = 1)
	private String deleteState;//删除状态
	
	@Comment("此用户回答此问题的点赞数")
	@Column(name = "sum_praise", length = 16)
	private Integer sumPraise;//此用户回答此问题的点赞数
	
	@Comment("用户对这个问题的回答的总评论数")
	@Column(name = "sum_comments", length = 16)
	private Integer sumComments;//用户对这个问题的回答的总评论数


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}

	public Integer getSumPraise() {
		return sumPraise;
	}

	public void setSumPraise(Integer sumPraise) {
		this.sumPraise = sumPraise;
	}

	public Integer getSumComments() {
		return sumComments;
	}

	public void setSumComments(Integer sumComments) {
		this.sumComments = sumComments;
	}
	
	
}
