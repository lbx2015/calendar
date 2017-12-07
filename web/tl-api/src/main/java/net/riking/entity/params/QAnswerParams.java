package net.riking.entity.params;

import net.riking.entity.BaseEntity;

/**
 * 问题回答类的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class QAnswerParams extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6952211991459875719L;

	// 问题回答ID
	private String questAnswerId;

	// 用户id
	private String userId;

	// 内容
	private String content;

	// 1-点赞；2-收藏
	private Integer optType;

	// 0-取消；1-赞同/收藏
	private Integer enabled;

	public String getQuestAnswerId() {
		return questAnswerId;
	}

	public void setQuestAnswerId(String questAnswerId) {
		this.questAnswerId = questAnswerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getOptType() {
		return optType;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

}
