package net.riking.entity.model;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

public class AppUserResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418351387803616527L;

	@Comment("物理主键")
	@JsonProperty("userId")
	private String id;

	@Comment("姓名")
	private String userName;

	@Comment("头像url")
	private String photoUrl;

	@Comment("经验值")
	private Integer experience;

	@Comment("用户描述")
	private String descript;

	@Comment("等级")
	private Integer grade;

	@Comment("回答数")
	private Integer answerNum;

	@Comment("点赞数")
	private Integer agreeNum;

	@Comment("是否已关注 0-未关注，1-已关注")
	private Integer isFollow;

	@Comment("是否已邀请 0-未邀请，1-已邀请")
	private Integer isInvited;

	public AppUserResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppUserResult(String id, String userName, String photoUrl, Integer experience, String userId) {
		super();
		this.id = id;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.experience = experience;
		if (StringUtils.isNotBlank(userId)) {
			isFollow = 1;// 已关注
		} else {
			isFollow = 0;// 未关注
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Integer getIsInvited() {
		return isInvited;
	}

	public void setIsInvited(Integer isInvited) {
		this.isInvited = isInvited;
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

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public Integer getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(Integer answerNum) {
		this.answerNum = answerNum;
	}

	public Integer getAgreeNum() {
		return agreeNum;
	}

	public void setAgreeNum(Integer agreeNum) {
		this.agreeNum = agreeNum;
	}

	public Integer getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(Integer isFollow) {
		this.isFollow = isFollow;
	}

}
