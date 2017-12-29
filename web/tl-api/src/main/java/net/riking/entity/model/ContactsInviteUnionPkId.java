package net.riking.entity.model;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * 资讯的评论点赞表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class ContactsInviteUnionPkId extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5586968998456004334L;

	@Comment("操作人主键  ")
	private String userId;

	@Comment("手机号")
	private String phone;

	public ContactsInviteUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ContactsInviteUnionPkId(String userId, String phone) {
		super();
		this.userId = userId;
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
		ContactsInviteUnionPkId other = (ContactsInviteUnionPkId) obj;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
