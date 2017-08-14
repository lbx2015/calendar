package net.riking.entity.model;

public class AliSme {
	// 电话号码
	private String phoneNumbers;
	// 短信签名
	private String signName;
	// 短信模板
	private String templateCode;
	// 验证码
	private String verificationCode;

	public AliSme() {
		super();
	}
	public AliSme(String phoneNumbers, String signName, String templateCode, String verificationCode) {
		this.phoneNumbers = phoneNumbers;
		this.signName = signName;
		this.templateCode = templateCode;
		this.verificationCode = verificationCode;
	}

	public String getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(String phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

}
