package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

public class UserOperationInfo extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8909823127178172847L;

	public int followNum;

	public int answerNum;

	public int fansNum;

	// 1-已签到 0-未签到
	public int signStatus;

	public int getFollowNum() {
		return followNum;
	}

	public void setFollowNum(int followNum) {
		this.followNum = followNum;
	}

	public int getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(int answerNum) {
		this.answerNum = answerNum;
	}

	public int getFansNum() {
		return fansNum;
	}

	public void setFansNum(int fansNum) {
		this.fansNum = fansNum;
	}

	public int getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(int signStatus) {
		this.signStatus = signStatus;
	}

}
