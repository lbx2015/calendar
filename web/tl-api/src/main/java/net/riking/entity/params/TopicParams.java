package net.riking.entity.params;

import net.riking.core.entity.BaseEntity;

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

	private Integer pindex; // 页数

	private Integer pcount;// 每页条数

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

	public Integer getPindex() {
		return pindex;
	}

	public void setPindex(Integer pindex) {
		this.pindex = pindex;
	}

	public Integer getPcount() {
		return pcount;
	}

	public void setPcount(Integer pcount) {
		this.pcount = pcount;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

}
