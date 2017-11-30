package net.riking.entity.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("用户详情表")
@Entity
@Table(name = "t_appuser_detail")
public class AppUserDetail implements Serializable {

	public AppUserDetail() {
		super();
	}

	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "assigned")
	@Column(name = "ID", length = 32)
	@Comment("pk 同用户登录表t_app_user的id一致")
	private String id;

	@Comment("真实姓名")
	@Column(name = "real_name", length = 32)
	private String realName;

	@Comment("用户邮箱")
	@Column(name = "email", length = 32)
	private String email;

	@Comment("用户公司")
	@Column(name = "company_name", length = 32)
	private String companyName;

	@Comment("用户性别")
	@Column(name = "sex")
	private Integer sex;

	@Comment("用户生日 (yyyyMMdd)")
	@Column(name = "birthday", length = 8)
	private String birthday;

	@Comment("用户地址")
	@Column(name = "address", length = 32)
	private String address;

	@Comment("用户描述")
	@Column(name = "description", length = 500)
	private String description;

	@Comment("手机Macid")
	@Column(name = "phone_macid", length = 32)
	private String phoneMacid;

	@Comment("手机类型 1-IOS;2-Android;3-其它")
	@Column(name = "phone_type", length = 1)
	private String phoneType;

	@Comment("积分")
	@Column(name = "integral")
	private Integer integral;

	@Comment("经验值")
	@Column(name = "experience")
	private Integer experience;

	@Comment("用户头像（存放用户头像名称）")
	@Column(name = "photo_url", length = 128)
	private String photoUrl;

	@Comment("全天提醒时间 HHmm时分")
	@Column(name = "remind_time", length = 4)
	private String remindTime;

	@Comment("是否已订阅: 0-未订阅；1-已订阅")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "is_subscribe", length = 1)
	private Integer isSubscribe;

	@Comment("行业ID")
	@Column(name = "industry_id")
	private Integer industryId;

	@Comment("职位ID")
	@Column(name = "position_id")
	private Integer positionId;

	@Comment("是否引导: 0-未引导；1-已引导")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "is_guide")
	private Integer isGuide;

	// @Comment("证件类型")
	// @Column(name = "id_type", length = 4)
	// private String idType;
	//
	// @Comment("证件号码")
	// @Column(name = "id_code", length = 32)
	// private String idCode;

	// @Comment("备注信息")
	// @Column(name = "remark", length = 500)
	// private String remark;
	
	@Transient
	private String page;// 页数

	@Transient
	private String sTime;// 查询时间

	// 验证码
	@Transient
	private String valiCode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
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

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String getValiCode() {
		return valiCode;
	}

	public void setValiCode(String valiCode) {
		this.valiCode = valiCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppUserDetail other = (AppUserDetail) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
