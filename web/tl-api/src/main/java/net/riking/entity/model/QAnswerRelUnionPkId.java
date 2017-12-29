package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

/**
 * 问题回答点赞和收藏表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class QAnswerRelUnionPkId extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4937883106951332430L;

	private String userId;

	private String qaId;

	private Integer dataType;

	public QAnswerRelUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QAnswerRelUnionPkId(String userId, String qaId, Integer dataType) {
		super();
		this.userId = userId;
		this.qaId = qaId;
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

	public String getQaId() {
		return qaId;
	}

	public void setQaId(String qaId) {
		this.qaId = qaId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((qaId == null) ? 0 : qaId.hashCode());
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
		QAnswerRelUnionPkId other = (QAnswerRelUnionPkId) obj;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (qaId == null) {
			if (other.qaId != null)
				return false;
		} else if (!qaId.equals(other.qaId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
