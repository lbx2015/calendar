package net.riking.entity.params;

import java.util.List;

import net.riking.core.entity.PageQuery;

/**
 * 资讯类的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class UserParams extends PageQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户Id
	private String userId;

	// 验证码
	private String verifyCode;

	// 是否删除： 0-删除，1-未删除
	private Integer isDeleted;

	// 手机号
	private String phone;

	// 手机Deviceid
	private String phoneDeviceid;

	// 手机类型 1-IOS;2-Android;3-其它
	private Integer phoneType;

	private List<String> phones;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Integer getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(Integer phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneDeviceid() {
		return phoneDeviceid;
	}

	public void setPhoneDeviceid(String phoneDeviceid) {
		this.phoneDeviceid = phoneDeviceid;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

}
