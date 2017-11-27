package net.riking.entity.model;

import java.io.Serializable;

/**
 * 话题关注表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class TopicRelUnionPkId implements Serializable {

	private static final long serialVersionUID = 944410490844286677L;

	private String userId;

	private String topicId;

	public TopicRelUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TopicRelUnionPkId(String userId, String topicId) {
		super();
		this.userId = userId;
		this.topicId = topicId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((topicId == null) ? 0 : topicId.hashCode());
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
		TopicRelUnionPkId other = (TopicRelUnionPkId) obj;
		if (topicId == null) {
			if (other.topicId != null)
				return false;
		} else if (!topicId.equals(other.topicId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
