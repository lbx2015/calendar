package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

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

	// 用户名
	@Transient
	private String userName;

	// 用户头像
	@Transient
	private String photoUrl;

	public QAComment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QAComment(String id, Date createdTime, Date modifiedTime, Integer isAduit, String userId,
			String questionAnswerId, String content, String userName, String photoUrl) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.setIsAduit(isAduit);
		this.userId = userId;
		this.questionAnswerId = questionAnswerId;
		this.content = content;
		this.userName = userName;
		this.photoUrl = photoUrl;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

}
