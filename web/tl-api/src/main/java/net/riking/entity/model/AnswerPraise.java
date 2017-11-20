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
 * @version crateTime：2017年11月18日 下午6:23:25
 * @used 回答点赞表
 */
@Entity
@Table(name = "t_answer_praise")
public class AnswerPraise {
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Comment("点赞的用户id")
	@Column(name = "user_id", length = 32)
	private String userId;//点赞的用户id
	
	@Comment("点赞的问题回答id")
	@Column(name = "question_answer_id", length = 32)
	private String questionAnswerId;//点赞的问题回答id
	
	@Comment("点赞的回答所属的问题id")
	@Column(name = "question_id", length = 32)
	private String questionId;//点赞的回答所属的问题
	
	@Comment("点赞的时间")
	@Column(name = "create_time", length = 16)
	private String createTime;//点赞的时间
	
	@Comment("删除标志")
	@Column(name = "delete_state", length = 1)
	private String deleteState;//删除标志


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}

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

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
