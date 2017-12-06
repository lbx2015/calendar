package net.riking.entity.params;

import java.io.Serializable;

/**
 * 更新用户信息参数接收
 * @author james.you
 * @version crateTime：2017年11月29日 下午3:16:14
 * @used TODO
 */
public class UpdUserParams implements Serializable {
	// 用户ID
	private String userId;

	// 用户名称
	private String userName;

	// 微信openid
	private String openId;

	// 用户邮箱
	private String email;

	// 手机号
	private String phone;

	// 用户状态 0-禁用 1-启用
	private String enabled;

	// 真实姓名
	private String realName;

	// 公司名称
	private String companyName;

	// 用户性别:1-男,0-女
	private Integer sex;

	// 出身日期(yyyyMMdd)
	private String birthday;

	// 地址
	private String address;

	// 个性签名
	private String description;

	// 手机Deviceid
	private String phoneDeviceid;

	// 积分
	private Integer integral;

	// 经验值
	private Integer experience;

	// 用户头像（存放用户头像名称）
	private String photoUrl;

	// 全天提醒时间 HHmm时分
	private String remindTime;

	// 是否已订阅: 0-未订阅；1-已订阅
	private Integer isSubscribe;

	// 行业ID
	private Integer industryId;

	// 职位ID
	private Integer positionId;

	// 是否引导: 0-未引导；1-已引导
	private Integer isGuide;

	public UpdUserParams() {
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

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
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

	public String getPhoneDeviceid() {
		return phoneDeviceid;
	}

	public void setPhoneDeviceid(String phoneDeviceid) {
		this.phoneDeviceid = phoneDeviceid;
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