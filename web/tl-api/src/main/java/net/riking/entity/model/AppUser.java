package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Entity
@Table(name = "t_app_user")
public class AppUser extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5370380117246181873L;

	public AppUser() {
		super();
	}

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "assigned")
	@JsonProperty("appUserId")
	private String id;

	@Comment("用户名称")
	@Column(name = "user_name", length = 32, nullable = false)
	private String userName;

	@Comment("微信openid")
	@Column(name = "open_id", length = 128)
	private String openId;

	@Comment("用户邮箱")
	@Column(name = "email", length = 32)
	private String email;

	@Comment("用户邮箱是否已认证： 0-未认证 1-已认证")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "is_identified")
	private Integer isIdentified;

	@Comment("手机号")
	@Column(name = "phone", length = 11, nullable = false)
	private String phone;

	@Comment("用户登录密码")
	@Column(name = "password", length = 64)
	private String passWord;

	@Comment("用户状态 0-禁用 1-启用")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "enabled", nullable = false, precision = 1)
	private Integer enabled;

	@Comment("创建人ID")
	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@Comment("修改人ID")
	@Column(name = "modified_by")
	private String modifiedBy;

	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;

	@Comment("修改时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.UpdateTimestamp
	@Column(name = "modified_time", insertable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date modifiedTime;

	@Comment("是否删除： 0-删除，1-未删除")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "is_deleted", insertable = false, nullable = false, precision = 1)
	private Integer isDeleted;

	/**
	 * 用户详情信息
	 */
	@Transient
	private AppUserDetail detail;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getIsIdentified() {
		return isIdentified;
	}

	public void setIsIdentified(Integer isIdentified) {
		this.isIdentified = isIdentified;
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

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public AppUserDetail getDetail() {
		return detail;
	}

	public void setDetail(AppUserDetail detail) {
		this.detail = detail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

}
