package net.riking.entity.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

public class TQuestionResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418351387803616527L;

	@Comment("问题主键")
	@JsonProperty("tqId")
	private String tqId;

	@Comment("问题标题")
	private String tqTitle;

	@Comment("话题的标题")
	private String topicTitle;

	@Comment("创建时间")
	private Date createdTime;

	@Comment("话题的头像")
	private String topicUrl;

	@Comment("问题的回答内容")
	private String qaContent;

	@Comment("话题关注数")
	private Integer tfollowNum;

	@Comment("用户Id")
	private String userId;

	@Comment("用户名")
	private String userName;

	@Comment("用户头像")
	private String photoUrl;

	@Comment("经验值")
	private Integer experience;

	@Comment("问题回答的评论数")
	private Integer qaCommentNum;

	@Comment("问题回答的点赞数")
	private Integer qaAgreeNum;

	@Comment("问题的关注数")
	private Integer qfollowNum;

	@Comment("问题的回答数")
	private Integer qanswerNum;

	@Comment("推送类型 1-根据用户关注的话题推送问题；2-关注的用户点赞的回答；3-关注的用户关注的问题；4-关注的用户回答的问题")
	private Integer pushType;

	public TQuestionResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TQuestionResult(String tqId, String tqTitle, String topicTitle, String topicUrl, String qaContent,
			Integer tfollowNum, String userId, String userName, String photoUrl, Integer experience,
			Integer qaCommentNum, Integer qaAgreeNum, Integer qfollowNum, Integer qanswerNum, Integer pushType) {
		super();
		this.tqId = tqId;
		this.tqTitle = tqTitle;
		this.topicTitle = topicTitle;
		this.topicUrl = topicUrl;
		this.qaContent = qaContent;
		this.tfollowNum = tfollowNum;
		this.userId = userId;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.experience = experience;
		this.qaCommentNum = qaCommentNum;
		this.qaAgreeNum = qaAgreeNum;
		this.qfollowNum = qfollowNum;
		this.qanswerNum = qanswerNum;
		this.pushType = pushType;
	}

	public String getTqId() {
		return tqId;
	}

	public void setTqId(String tqId) {
		this.tqId = tqId;
	}

	public String getTqTitle() {
		return tqTitle;
	}

	public void setTqTitle(String tqTitle) {
		this.tqTitle = tqTitle;
	}

	public String getTopicTitle() {
		return topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	public String getTopicUrl() {
		return topicUrl;
	}

	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}

	public Integer getTfollowNum() {
		return tfollowNum;
	}

	public void setTfollowNum(Integer tfollowNum) {
		this.tfollowNum = tfollowNum;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getQaContent() {
		return qaContent;
	}

	public void setQaContent(String qaContent) {
		this.qaContent = qaContent;
	}

	public Integer getQaCommentNum() {
		return qaCommentNum;
	}

	public void setQaCommentNum(Integer qaCommentNum) {
		this.qaCommentNum = qaCommentNum;
	}

	public Integer getQaAgreeNum() {
		return qaAgreeNum;
	}

	public void setQaAgreeNum(Integer qaAgreeNum) {
		this.qaAgreeNum = qaAgreeNum;
	}

	public Integer getQfollowNum() {
		return qfollowNum;
	}

	public void setQfollowNum(Integer qfollowNum) {
		this.qfollowNum = qfollowNum;
	}

	public Integer getQanswerNum() {
		return qanswerNum;
	}

	public void setQanswerNum(Integer qanswerNum) {
		this.qanswerNum = qanswerNum;
	}

	public Integer getPushType() {
		return pushType;
	}

	public void setPushType(Integer pushType) {
		this.pushType = pushType;
	}

}
