package net.riking.entity.model;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

public class TopicResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418351387803616527L;

	@Comment("物理主键")
	@JsonProperty("topicId")
	private String id;

	@Comment("标题")
	private String title;

	@Comment("话题url")
	private String topicUrl;

	@Comment("关注数")
	private Integer followNum;

	@Comment("是否已关注 0-未关注，1-已关注")
	private Integer isFollow;

	public TopicResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TopicResult(String id, String title, String topicUrl, String topicId) {
		super();
		this.id = id;
		this.title = title;
		this.topicUrl = topicUrl;
		if (StringUtils.isNotBlank(topicId)) {
			this.isFollow = 1;// 已关注
		} else {
			this.isFollow = 0;// 未关注
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTopicUrl() {
		return topicUrl;
	}

	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}

	public Integer getFollowNum() {
		return followNum;
	}

	public void setFollowNum(Integer followNum) {
		this.followNum = followNum;
	}

	public Integer getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(Integer isFollow) {
		this.isFollow = isFollow;
	}

}
