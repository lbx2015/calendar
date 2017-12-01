package net.riking.entity.params;

import java.io.Serializable;

/**
 * 资讯类的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class UserParams implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户Id
	private String userId;

	// 是否删除： 0-删除，1-未删除
	private Integer isDeleted;

	// 手机Macid
	private String phoneMacid;

	// 手机类型 1-IOS;2-Android;3-其它
	private Integer phoneType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(Integer phoneType) {
		this.phoneType = phoneType;
	}

	public String getPhoneMacid() {
		return phoneMacid;
	}

	public void setPhoneMacid(String phoneMacid) {
		this.phoneMacid = phoneMacid;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

}
