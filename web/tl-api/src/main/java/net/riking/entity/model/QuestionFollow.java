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
 * @version crateTime：2017年11月15日 下午2:20:10
 * @used 问题关注表
 */
@Entity
@Table(name = "t_question_follow")
public class QuestionFollow {
	
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Comment("问题id")
	@Column(name = "question_id", length = 32)
	private String questionId;//问题id
	
	@Comment("用户id")
	@Column(name = "user_id", length = 32)
	private String userId;//用户id
	
	@Comment("创建时间")
	@Column(name = "create_time", length = 16)
	private String createTime;//创建时间/关注话题的时间
	
	@Comment("删除标志")
	@Column(name = "delete_state", length = 1)
	private String deleteState;//删除标志 0：删除 ； 1：未删除

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
	
	

}
