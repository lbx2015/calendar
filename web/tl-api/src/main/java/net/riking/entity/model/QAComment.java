package net.riking.entity.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = -1580621958798681642L;

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

	// 点赞数
	@Transient
	private Integer agreeNumber;

	// 经验值
	@Transient
	private Integer experience;

	@Transient
	@Comment("是否已点赞 0-未点赞，1-已点赞")
	private Integer isAgree;

	// 问题回答评论的回复list
	@Transient
	List<Map<String, Object>> qACReplyList;

	public QAComment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QAComment(String id, Date createdTime, Date modifiedTime, Integer isAudit, String userId,
			String questionAnswerId, String content, String userName, String photoUrl, Integer experience) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.setIsAudit(isAudit);
		this.userId = userId;
		this.questionAnswerId = questionAnswerId;
		this.content = content;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.experience = experience;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
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

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Map<String, Object>> getQACReplyList() {
		if (qACReplyList == null) {
			qACReplyList = new ArrayList<Map<String, Object>>();
		}
		return this.qACReplyList;
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

	public Integer getAgreeNumber() {
		return agreeNumber;
	}

	public void setAgreeNumber(Integer agreeNumber) {
		this.agreeNumber = agreeNumber;
	}

}
