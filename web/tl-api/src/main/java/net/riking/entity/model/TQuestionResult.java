package net.riking.entity.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

public class TQuestionResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6032956463780926056L;

	@Comment("问题主键")
	private String tqId;

	@Comment("问题回答主键")
	private String qaId;

	@Comment("问题标题")
	private String tqTitle;

	@Comment("话题的标题")
	private String topicTitle;

	// 创建时间
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date createdTime;

	@Comment("问题的回答内容")
	private String qaContent;

	@Comment("用户Id")
	private String userId;

	@Comment("用户名")
	private String userName;

	@Comment("用户/话题头像")
	private String fromImgUrl;

	@Comment("经验值")
	private Integer experience;

	@Comment("问题回答的评论数")
	private Integer qaCommentNum;

	@Comment("问题回答的点赞数")
	private Integer qaAgreeNum;

	@Comment("问题的关注数")
	private Integer qfollowNum;

	@Comment("话题关注数")
	private Integer tfollowNum;

	@Comment("问题的回答数")
	private Integer qanswerNum;

	@Comment("回答封面url")
	private String coverUrl;

	@Comment("推送类型 1-根据用户关注的话题推送问题；2-关注的用户点赞的回答；3-关注的用户关注的问题；4-关注的用户回答的问题")
	private Integer pushType;

	@Comment("可能感兴趣的话题")
	private List<TopicResult> topicResults;

	@Comment("可能感兴趣的人")
	private List<AppUserResult> appUserResults;

	public TQuestionResult() {
		super();
	}

	public TQuestionResult(String tqId, String qaId, String tqTitle, String topicTitle, Date createdTime,
			String qaContent, Integer tfollowNum, String userId, String userName, String fromImgUrl, Integer experience,
			Integer qaCommentNum, Integer qaAgreeNum, Integer qfollowNum, Integer qanswerNum, Integer pushType) {
		super();
		this.tqId = tqId;
		this.qaId = qaId;
		this.tqTitle = tqTitle;
		this.topicTitle = topicTitle;
		this.createdTime = createdTime;
		this.qaContent = qaContent;
		this.tfollowNum = tfollowNum;
		this.userId = userId;
		this.userName = userName;
		this.fromImgUrl = fromImgUrl;
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

	public List<TopicResult> getTopicResults() {
		return topicResults;
	}

	public void setTopicResults(List<TopicResult> topicResults) {
		this.topicResults = topicResults;
	}

	public List<AppUserResult> getAppUserResults() {
		return appUserResults;
	}

	public void setAppUserResults(List<AppUserResult> appUserResults) {
		this.appUserResults = appUserResults;
	}

	public String getQaId() {
		return qaId;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public void setQaId(String qaId) {
		this.qaId = qaId;
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

	public String getFromImgUrl() {
		return fromImgUrl;
	}

	public void setFromImgUrl(String fromImgUrl) {
		this.fromImgUrl = fromImgUrl;
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
