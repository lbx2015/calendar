package net.riking.entity.resp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

/**
 * 其他用户信息
 * 
 * @author jc.tan 2017年12月21日
 * @see
 * @since 1.0
 */
public class OtherUserResp extends BaseEntity {
	// 其他用户ID
	private String userId;

	// 用户名称
	private String userName;

	// 用户性别:1-男,0-女
	private Integer sex;

	// 个性签名
	private String descript;

	// 经验值
	@JsonIgnore
	private Integer experience;

	// 等级
	private Integer grade;

	// 用户头像（存放用户头像名称）
	private String photoUrl;

	// 回答数
	private Integer answerNum;

	// 关注数
	private Integer followNum;

	// 粉丝
	private Integer fansNum;

	// 是否已关注
	private Integer isFollow;
	
	@Comment("允许查看我的动态: 0-不可以；1-可以查看")
	private Integer checkMyDynamicState;

	// 查看我的关注
	@Comment("允许查看我的关注: 0-不可以；1-可以查看")
	private Integer checkMyFollowState;

	// 查看我的收藏
	@Comment("允许查看我的收藏: 0-不可以；1-可以查看")
	private Integer checkMyCollectState;
	
	

	public OtherUserResp(String userId, String userName, Integer sex,
			String descript, Integer experience, String photoUrl,
			Integer followStatus, Integer checkMyDynamicState,
			Integer checkMyFollowState, Integer checkMyCollectState) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.sex = sex;
		this.descript = descript;
		this.experience = experience;
		this.photoUrl = photoUrl;
		if (followStatus == null || followStatus == 0) {
			this.isFollow = 0;
		} else if (followStatus == 1) {
			this.isFollow = 1;
		} else if (followStatus == 2) {
			this.isFollow = 2;
		}
		this.checkMyDynamicState = checkMyDynamicState;
		this.checkMyFollowState = checkMyFollowState;
		this.checkMyCollectState = checkMyCollectState;
	}

	public OtherUserResp(String userId, String userName, Integer sex, String descript, Integer experience,
			String photoUrl, Integer followStatus) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.sex = sex;
		this.descript = descript;
		this.experience = experience;
		this.photoUrl = photoUrl;
		if (followStatus == null || followStatus == 0) {
			this.isFollow = 0;
		} else if (followStatus == 1) {
			this.isFollow = 1;
		} else if (followStatus == 2) {
			this.isFollow = 2;
		}
	}

	public OtherUserResp() {
		super();
		// TODO Auto-generated constructor stub
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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Integer getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(Integer answerNum) {
		this.answerNum = answerNum;
	}

	public Integer getFollowNum() {
		return followNum;
	}

	public void setFollowNum(Integer followNum) {
		this.followNum = followNum;
	}

	public Integer getFansNum() {
		return fansNum;
	}

	public void setFansNum(Integer fansNum) {
		this.fansNum = fansNum;
	}

	public Integer getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(Integer isFollow) {
		this.isFollow = isFollow;
	}

	public Integer getCheckMyDynamicState() {
		return checkMyDynamicState;
	}

	public void setCheckMyDynamicState(Integer checkMyDynamicState) {
		this.checkMyDynamicState = checkMyDynamicState;
	}

	public Integer getCheckMyFollowState() {
		return checkMyFollowState;
	}

	public void setCheckMyFollowState(Integer checkMyFollowState) {
		this.checkMyFollowState = checkMyFollowState;
	}

	public Integer getCheckMyCollectState() {
		return checkMyCollectState;
	}

	public void setCheckMyCollectState(Integer checkMyCollectState) {
		this.checkMyCollectState = checkMyCollectState;
	}

	
	
}
