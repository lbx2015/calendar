package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

/**
 * 话题的问题关注表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class TQRelUnionPkId extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2680078337804032616L;

	private String userId;

	private String tqId;

	private Integer dataType;

	public TQRelUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TQRelUnionPkId(String userId, String tqId, Integer dataType) {
		super();
		this.userId = userId;
		this.tqId = tqId;
		this.dataType = dataType;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTqId() {
		return tqId;
	}

	public void setTqId(String tqId) {
		this.tqId = tqId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((tqId == null) ? 0 : tqId.hashCode());
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
		TQRelUnionPkId other = (TQRelUnionPkId) obj;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (tqId == null) {
			if (other.tqId != null)
				return false;
		} else if (!tqId.equals(other.tqId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
