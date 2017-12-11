package net.riking.entity.model;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * 问题回答点赞和收藏表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class QAInviteUnionPkId extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4937883106951332430L;

	@Comment("操作人主键  ")
	private String userId;

	@Comment("问题主键")
	private String questionId;

	@Comment("被邀请人主键")
	private String toUserId;

	public QAInviteUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QAInviteUnionPkId(String userId, String questionId, String toUserId) {
		super();
		this.userId = userId;
		this.questionId = questionId;
		this.toUserId = toUserId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
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
		result = prime * result + ((questionId == null) ? 0 : questionId.hashCode());
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
		QAInviteUnionPkId other = (QAInviteUnionPkId) obj;
		if (questionId == null) {
			if (other.questionId != null)
				return false;
		} else if (!questionId.equals(other.questionId))
			return false;
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
