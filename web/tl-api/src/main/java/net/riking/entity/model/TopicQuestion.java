package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseAuditProp;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("话题的问题表")
@Entity
@Table(name = "t_topic_question")
public class TopicQuestion extends BaseAuditProp {

	@Comment("标题")
	@Column(name = "title", length = 512, nullable = false)
	private String title;

	@Comment("内容")
	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	@Comment("话题主键： fk t_topic (最多3个，用','分隔开)") // 最多3个
	@Column(name = "topic_id", length = 128, nullable = false)
	private String topicId;

	@Comment("用户主键: fk t_app_user")
	@Column(name = "user_id", length = 32)
	private String userId;

	// 用户名
	@Transient
	private String userName;

	// @Comment("用户关注数")
	// @org.hibernate.annotations.ColumnDefault("0")
	// @Column(name="follow_num",insertable=false, nullable=false)
	// private Integer followNum;
	//
	// @Comment("用户回答数")
	// @org.hibernate.annotations.ColumnDefault("0")
	// @Column(name="answer_num",insertable=false, nullable=false)
	// private Integer answerNum;

	public TopicQuestion(String id, Date createdTime, Integer isAduit, String title, String content, String topicId,
			String userId, String userName) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setIsAduit(isAduit);
		this.title = title;
		this.content = content;
		this.topicId = topicId;
		this.userId = userId;
		this.userName = userName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

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

	// public Integer getFollowNum() {
	// return followNum;
	// }
	//
	// public void setFollowNum(Integer followNum) {
	// this.followNum = followNum;
	// }
	//
	// public Integer getAnswerNum() {
	// return answerNum;
	// }
	//
	// public void setAnswerNum(Integer answerNum) {
	// this.answerNum = answerNum;
	// }

}
