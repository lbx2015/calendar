package net.riking.entity.model;

import java.io.Serializable;

import net.riking.core.annos.Comment;

public class AppUserResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418351387803616527L;

	@Comment("物理主键")
	private String id;

	@Comment("姓名")
	private String userName;

	@Comment("头像url")
	private String photoUrl;

	@Comment("经验值")
	private Integer experience;

	@Comment("回答数")
	private Integer answerNum;

	@Comment("点赞数")
	private Integer agreeNum;

	@Comment("是否已关注 0-未关注，1-已关注")
	private Integer isFollow;

	public AppUserResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppUserResult(String id, String userName, String photoUrl, Integer experience) {
		super();
		this.id = id;
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
