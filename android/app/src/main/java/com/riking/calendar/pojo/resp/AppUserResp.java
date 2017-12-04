package com.riking.calendar.pojo.resp;

import java.io.Serializable;

/**
 * APP用户信息返回
 */
public class AppUserResp implements Serializable {
	// 用户ID
	public String userId;

	// 用户名称
	public String userName;

	// 微信openid
	public String openId;

	// 用户邮箱
	public String email;

	// 手机号
	public String phone;

	// 真实姓名
	public String realName;

	// 公司名称
	public String companyName;

	// 用户性别:1-男,0-女
	public Integer sex;

	// 出身日期(yyyyMMdd)
	public String birthday;

	// 地址
	public String address;

	// 个性签名
	public String description;

	// 手机Macid
	public String phoneMacid;

	// 积分
	public Integer integral;

	// 经验值
	public Integer experience;

	// 用户头像（存放用户头像名称）
	public String photoUrl;

	// 全天提醒时间 HHmm时分
	public String remindTime;

	// 是否已订阅: 0-未订阅；1-已订阅
	public Integer isSubscribe;

	// 行业ID
	public Integer industryId;

	// 职位ID
	public Integer positionId;

	// 是否引导: 0-未引导；1-已引导
	public Integer isGuide;

	public AppUserResp(String userId, String userName, String openId, String email, String phone, String realName,
			String companyName, Integer sex, String birthday, String address, String description, String phoneMacid,
			Integer integral, Integer experience, String photoUrl, String remindTime, Integer isSubscribe,
			Integer industryId, Integer positionId, Integer isGuide) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.openId = openId;
		this.email = email;
		this.phone = phone;
		this.realName = realName;
		this.companyName = companyName;
		this.sex = sex;
		this.birthday = birthday;
		this.address = address;
		this.description = description;
		this.phoneMacid = phoneMacid;
		this.integral = integral;
		this.experience = experience;
		this.photoUrl = photoUrl;
		this.remindTime = remindTime;
		this.isSubscribe = isSubscribe;
		this.industryId = industryId;
		this.positionId = positionId;
		this.isGuide = isGuide;
	}

	public AppUserResp() {
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

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhoneMacid() {
		return phoneMacid;
	}

	public void setPhoneMacid(String phoneMacid) {
		this.phoneMacid = phoneMacid;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(String remindTime) {
		this.remindTime = remindTime;
	}

	public Integer getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(Integer isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

	public Integer getIndustryId() {
		return industryId;
	}

	public void setIndustryId(Integer industryId) {
		this.industryId = industryId;
	}

	public Integer getPositionId() {
		return positionId;
	}

	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}

	public Integer getIsGuide() {
		return isGuide;
	}

	public void setIsGuide(Integer isGuide) {
		this.isGuide = isGuide;
	}

}
