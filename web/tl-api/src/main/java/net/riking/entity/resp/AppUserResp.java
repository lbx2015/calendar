package net.riking.entity.resp;

import net.riking.entity.BaseEntity;

/**
 * APP用户信息返回
 * @author james.you
 * @version crateTime：2017年11月29日 下午3:16:14
 * @used TODO
 */
public class AppUserResp extends BaseEntity {
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
	private String descript;

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

	public AppUserResp(String userId, String userName, String openId, String email, String phone, String realName,
			String companyName, Integer sex, String birthday, String address, String descript, String phoneDeviceid,
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
		this.descript = descript;
		this.phoneDeviceid = phoneDeviceid;
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

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
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
