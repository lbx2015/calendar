package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

/**
 * 系统消息联合主键
 * @author james.you
 * @version crateTime：2018年1月3日 下午4:20:00
 * @used TODO
 */
public class SysNoticeReadUnionPkId extends BaseEntity {

	private static final long serialVersionUID = 944410490844286677L;

	private String userId;

	private String noticeId;


	public SysNoticeReadUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SysNoticeReadUnionPkId(String userId, String noticeId) {
		super();
		this.userId = userId;
		this.noticeId = noticeId;
	}

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((noticeId == null) ? 0 : noticeId.hashCode());
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
		SysNoticeReadUnionPkId other = (SysNoticeReadUnionPkId) obj;
		
		if (noticeId == null) {
			if (other.noticeId != null)
				return false;
		} else if (!noticeId.equals(other.noticeId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
