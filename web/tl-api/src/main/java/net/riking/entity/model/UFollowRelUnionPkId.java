package net.riking.entity.model;

import java.io.Serializable;

/**
 * 关注用户 表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class UFollowRelUnionPkId implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8643643475165937658L;

	private String userId;

	private String toUserId;

	public UFollowRelUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UFollowRelUnionPkId(String userId, String toUserId) {
		super();
		this.userId = userId;
		this.toUserId = toUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((toUserId == null) ? 0 : toUserId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		UFollowRelUnionPkId other = (UFollowRelUnionPkId) obj;
		if (toUserId == null) {
			if (other.toUserId != null)
				return false;
		} else if (!toUserId.equals(other.toUserId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
