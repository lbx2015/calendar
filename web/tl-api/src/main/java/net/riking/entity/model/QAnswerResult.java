package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

public class QAnswerResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418351387803616527L;

	@Comment("回答id")
	private String tqId;

	@Comment("问题回答id")
	private String qaId;

	// 用户id
	@Transient
	private String userId;

	@Comment("标题")
	private String title;

	@Comment("内容")
	private String content;

	@Comment("创建时间")
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date createdTime;

	// 用户名
	@Transient
	private String userName;

	// 用户头像Url
	@Transient
	private String photoUrl;

	// 经验值
	@Transient
	private Integer experience;

	// 封面url
	@Transient
	private String coverUrl;

	// 问题回答点赞数
	@Transient
	private Integer qaAgreeNum;

	// 问题回答评论数
	@Transient
	private Integer qaCommentNum;

	// 是否已点赞 0-未点赞，1-已点赞
	@Transient
	private Integer isAgree;

	public QAnswerResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QAnswerResult(String tqId, String qaId, String userId, String title, String content, Date createdTime,
			String userName, String photoUrl, Integer experience, String coverUrl) {
		super();
		this.tqId = tqId;
		this.qaId = qaId;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.createdTime = createdTime;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.experience = experience;
		this.coverUrl = coverUrl;
	}

	public String getTqId() {
		return tqId;
	}

	public void setTqId(String tqId) {
		this.tqId = tqId;
	}

	public Integer getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
	}

	public String getQaId() {
		return qaId;
	}

	public void setQaId(String qaId) {
		this.qaId = qaId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getQaAgreeNum() {
		return qaAgreeNum;
	}

	public void setQaAgreeNum(Integer qaAgreeNum) {
		this.qaAgreeNum = qaAgreeNum;
	}

	public Integer getQaCommentNum() {
		return qaCommentNum;
	}

	public void setQaCommentNum(Integer qaCommentNum) {
		this.qaCommentNum = qaCommentNum;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

}
