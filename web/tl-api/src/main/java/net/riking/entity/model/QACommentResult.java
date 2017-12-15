package net.riking.entity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("我的动态问题回答评论列表")
public class QACommentResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1580621958798681642L;

	@JsonProperty("qACommentId")
	private String id;

	@Comment("创建时间")
	private Date createdTime;

	@Comment("修改时间")
	private Date modifiedTime;

	@Comment("操作人主键  ")
	private String userId;

	@Comment("问题回答id")
	private String questionAnswerId;

	@Comment("内容")
	private String content;

	@Comment("问题id")
	private String tqId;

	// 用户名
	private String userName;

	// 用户头像
	private String photoUrl;

	// 经验值
	private Integer experience;

	// 点赞数
	private Integer agreeNumber;

	// 等级
	private Integer grade;

	private Integer isAgree;

	@Comment("问题标题")
	private String tqTitle;

	public QACommentResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QACommentResult(String id, Date createdTime, Date modifiedTime, String userId, String questionAnswerId,
			String content, String tqId, String userName, String photoUrl, Integer experience) {
		super();
		this.id = id;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
		this.userId = userId;
		this.questionAnswerId = questionAnswerId;
		this.content = content;
		this.tqId = tqId;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.experience = experience;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
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

	public String getTqId() {
		return tqId;
	}

	public void setTqId(String tqId) {
		this.tqId = tqId;
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

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public Integer getAgreeNumber() {
		return agreeNumber;
	}

	public void setAgreeNumber(Integer agreeNumber) {
		this.agreeNumber = agreeNumber;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
	}

	public String getTqTitle() {
		return tqTitle;
	}

	public void setTqTitle(String tqTitle) {
		this.tqTitle = tqTitle;
	}

}
