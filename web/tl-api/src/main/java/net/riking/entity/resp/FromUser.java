package net.riking.entity.resp;

import net.riking.core.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class FromUser extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5370380117246181873L;

	public FromUser() {
		super();
	}

	// 被回复人id
	String userId;

	// 被回复人名称
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
