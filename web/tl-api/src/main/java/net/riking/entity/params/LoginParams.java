package net.riking.entity.params;

import net.riking.entity.BaseEntity;

/**
 * 登录及注册参数
 * @author james.you
 * @version crateTime：2017年11月29日 下午3:16:14
 * @used TODO
 */
public class LoginParams extends BaseEntity {
	private static final long serialVersionUID = 1L;

	// 手机号
	private String phone;

	// 微信openId
	private String openId;

	// 输入的验证码
	private String verifyCode;

	// 类型：1-手机号,2-微信号
	private Integer type;

	// 客户端类型：1-IOS；2-Android
	private Integer clientType;

	// 手机端设备Id
	private String phoneDeviceId;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

	public String getPhoneDeviceId() {
		return phoneDeviceId;
	}

	public void setPhoneDeviceId(String phoneDeviceId) {
		this.phoneDeviceId = phoneDeviceId;
	}

}
