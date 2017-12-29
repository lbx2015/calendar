package net.riking.entity.EO;

/**
 * 导入excel的数据
 * @author jc.lin
 *
 */
public class EmailSuffixEO {
	private String emailSuffix;

	private String enabled;

	private String remark;// 备注

	public String getEmailSuffix() {
		return emailSuffix;
	}

	public void setEmailSuffix(String emailSuffix) {
		this.emailSuffix = emailSuffix;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
