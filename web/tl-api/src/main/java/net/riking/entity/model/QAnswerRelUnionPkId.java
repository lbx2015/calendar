package net.riking.entity.model;

import java.io.Serializable;

/**
 * 问题回答点赞和收藏表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class QAnswerRelUnionPkId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4937883106951332430L;

	private String userId;

	private String qaId;

	public QAnswerRelUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QAnswerRelUnionPkId(String userId, String qaId) {
		super();
		this.userId = userId;
		this.qaId = qaId;
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
