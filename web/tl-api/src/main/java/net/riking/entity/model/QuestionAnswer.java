package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
@Comment("回答信息 表")
@Entity
@Table(name = "t_question_answer")
public class QuestionAnswer extends BaseAuditProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -449237590873319540L;

	@Comment("回答人主键: fk t_app_user")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Comment("问题主键: fk t_topic_question")
	@Column(name = "question_id", nullable = false)
	private String questionId;

	@Comment("回答内容")
	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	// @Comment("用户收藏数")
	// @org.hibernate.annotations.ColumnDefault("0")
	// @Column(name="collect_num",insertable=false, nullable=false)
	// private Integer collectNum;
	//

	// 用户名
	@Transient
	private String userName;

	// 用户评论数
	@Transient
	private Integer commentNum;

	// 用户点赞数
	@Transient
	private Integer agreeNum;

	// 用户头像路径
	@Transient
	private String photoUrl;

	// 问题的标题
	@Transient
	private String title;

	// 经验值
	@Transient
	private Integer experience;

	// 是否已点赞（0-未点赞；1-已点赞）
	@Transient
	private Integer isAgree;

	// 是否已收藏（0-未收藏；1-已收藏）
	@Transient
	private Integer isCollect;

	public QuestionAnswer(String id, Date createdTime, Date modifiedTime, String userId, String questionId,
			String content, String userName, String photoUrl, Integer experience) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.userId = userId;
		this.questionId = questionId;
		this.content = content;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.experience = experience;
	}

	public QuestionAnswer(String id, Date createdTime, Date modifiedTime, String userId, String questionId,
			String content, String userName, String photoUrl, Integer experience, String title) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.userId = userId;
		this.questionId = questionId;
		this.content = content;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.experience = experience;
		this.title = title;
	}

	public QuestionAnswer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(Integer isCollect) {
		this.isCollect = isCollect;
	}

	public Integer getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getAgreeNum() {
		return agreeNum;
	}

	public void setAgreeNum(Integer agreeNum) {
		this.agreeNum = agreeNum;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
