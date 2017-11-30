package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Entity
@Table(name = "t_app_user")
public class AppUser extends BaseProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppUser() {
		super();
	}

	@Comment("用户名称")
	@Column(name = "user_name", length = 32, nullable = false)
	private String userName;

	@Comment("微信openid")
	@Column(name = "open_id", length = 128)
	private String openId;

	@Comment("用户邮箱")
	@Column(name = "email", length = 32)
	private String email;

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

	/**
	 * 用户详情信息
	 */
	@Transient
	public AppUserDetail detail;

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

}
