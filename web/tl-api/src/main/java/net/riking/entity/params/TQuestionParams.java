package net.riking.entity.params;

import net.riking.entity.BaseEntity;

/**
 * 问题的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class TQuestionParams extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 用户Id
	private String userId;

	// 话题下面问题Id
	private String tqId;

	// (标题)
	private String title;

	// (话题id,逗号','分隔，最三个)
	private String topicId;

	// (1-问题；2-话题；3-用户)
	private Integer objType;

	// (目标对象ID)
	private String attentObjId;

	// (0-取消；1-关注)
	private Integer enabled;

	// 问题回答Id
	private String questAnswerId;

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getQuestAnswerId() {
		return questAnswerId;
	}

	public void setQuestAnswerId(String questAnswerId) {
		this.questAnswerId = questAnswerId;
	}

	public String getTqId() {
		return tqId;
	}

	public void setTqId(String tqId) {
		this.tqId = tqId;
	}

	public Integer getObjType() {
		return objType;
	}

	public void setObjType(Integer objType) {
		this.objType = objType;
	}

	public String getAttentObjId() {
		return attentObjId;
	}

	public void setAttentObjId(String attentObjId) {
		this.attentObjId = attentObjId;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

}
