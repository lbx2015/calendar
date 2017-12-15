package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

/**
 * 问题回答的评论点赞表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class QACARelUnionPkId extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6849696535382085570L;

	private String userId;

	private String qacId;

	public QACARelUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QACARelUnionPkId(String userId, String qacId) {
		super();
		this.userId = userId;
		this.qacId = qacId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQacId() {
		return qacId;
	}

	public void setQacId(String qacId) {
		this.qacId = qacId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((qacId == null) ? 0 : qacId.hashCode());
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
		QACARelUnionPkId other = (QACARelUnionPkId) obj;
		if (qacId == null) {
			if (other.qacId != null)
				return false;
		} else if (!qacId.equals(other.qacId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
