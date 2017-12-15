package net.riking.entity.resp;

import net.riking.core.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class ToUser extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5370380117246181873L;

	public ToUser() {
		super();
	}

	// 回复人id
	String userId;

	// 回复人名称
	String userName;

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

}
