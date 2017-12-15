package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

/**
 * 问题回答的优秀回答者
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class QAExcellentResp extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1580621958798681642L;

	// 用户id
	private String userId;

	// 用户名
	private String userName;

	// 用户头像
	private String photoUrl;

	// 问题的回答数
	private Integer qanswerNum;

	// 回答的点赞数
	private String qaAgreeNum;

	// 经验值
	private Integer experience;

	// 经验值
	private Integer grade;

	// 是否已关注 0-未关注，1-已关注
	private Integer isFollow;

	public QAExcellentResp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QAExcellentResp(String userId, String userName, String photoUrl, Integer qanswerNum, String qaAgreeNum,
			Integer experience, Integer isFollow) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.qanswerNum = qanswerNum;
		this.qaAgreeNum = qaAgreeNum;
		this.experience = experience;
		this.isFollow = isFollow;
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

	public Integer getQanswerNum() {
		return qanswerNum;
	}

	public void setQanswerNum(Integer qanswerNum) {
		this.qanswerNum = qanswerNum;
	}

	public String getQaAgreeNum() {
		return qaAgreeNum;
	}

	public void setQaAgreeNum(String qaAgreeNum) {
		this.qaAgreeNum = qaAgreeNum;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public Integer getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(Integer isFollow) {
		this.isFollow = isFollow;
	}

}
