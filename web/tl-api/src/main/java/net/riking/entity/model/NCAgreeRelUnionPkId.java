package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

/**
 * 资讯的评论点赞表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class NCAgreeRelUnionPkId extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5586968998456004334L;

	private String userId;

	private String ncId;

	private Integer dataType;

	public NCAgreeRelUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NCAgreeRelUnionPkId(String userId, String ncId, Integer dataType) {
		super();
		this.userId = userId;
		this.ncId = ncId;
		this.dataType = dataType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNcId() {
		return ncId;
	}

	public void setNcId(String ncId) {
		this.ncId = ncId;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((ncId == null) ? 0 : ncId.hashCode());
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
		NCAgreeRelUnionPkId other = (NCAgreeRelUnionPkId) obj;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (ncId == null) {
			if (other.ncId != null)
				return false;
		} else if (!ncId.equals(other.ncId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
