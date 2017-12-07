package net.riking.entity.params;

import net.riking.entity.BaseEntity;

/**
 * 话题的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class TopicParams extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户id
	private String userId;

	// 话题详情
	private String topicId;

	// 1-精华；2-问题；3-优秀回答者
	private Integer optType;

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

}
