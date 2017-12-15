package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("用户详情表")
@Entity
@Table(name = "t_appuser_detail")
public class AppUserDetail extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6491834829935023232L;

	public AppUserDetail() {
		super();
	}

	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "assigned")
	@Column(name = "id", length = 32)
	@Comment("pk 同用户登录表t_app_user的id一致")
	@JsonProperty("appUserDetailId")
	private String id;

	@Comment("真实姓名")
	@Column(name = "real_name", length = 32)
	private String realName;

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
	@Column(name = "descript", length = 500)
	private String descript;

	@Comment("手机Deviceid")
	@Column(name = "phone_device_id", length = 32)
	private String phoneDeviceid;

	@Comment("手机类型 1-IOS;2-Android;3-其它")
	@Column(name = "phone_type", length = 1)
	private Integer phoneType;

	@Comment("积分")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "integral")
	private Integer integral;

	@Comment("经验值")
	@org.hibernate.annotations.ColumnDefault("0")
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

	@Transient
	@Comment("我的等级")
	private Integer grade;

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

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
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

	public Integer getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(Integer phoneType) {
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
