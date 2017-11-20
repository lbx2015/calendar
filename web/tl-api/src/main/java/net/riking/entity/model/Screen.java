package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import net.riking.core.annos.Comment;

/**
 * 
 * @author tao.yuan
 * @version crateTime：2017年11月16日 上午11:47:59
 * @used 屏蔽表
 */
@Entity
@Table(name = "t_screen")
public class Screen {

	@Id
	@GeneratedValue
	@Column(name = "Id")
	private String id;
	
	@Comment("用户id")
	@Column(name = "user_id", length = 32)
	private String userId;//用户id
	
	@Comment("屏蔽的问题id")
	@Column(name = "question_id", length = 32)
	private String questionId;//屏蔽的问题id
	
	@Comment("删除状态")
	@Column(name = "delete_state", length = 1)
	private String deleteState;//删除状态 0.已删除 1.未删除
	
	@Comment("创建时间")
	@Column(name = "create_time", length = 1)
	private String createTime;//创建时间

	public String getId() {
		return id;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
