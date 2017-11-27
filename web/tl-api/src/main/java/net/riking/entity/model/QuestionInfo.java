package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("问题信息 表")
@Entity
@Table(name = "t_question_info")
public class QuestionInfo extends BaseProp {

	private static final long serialVersionUID = -5590305370304645973L;

	@Comment("标题")
	@Column(name = "title", length = 512, nullable = false)
	private String title;

	@Comment("内容")
	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	@Comment("话题主键") // 最多3个
	@Column(name = "topic_id", length = 99)
	private String topicId;

	@Comment("用户主键")
	@Column(name = "user_id", length = 32)
	private String userId;

	// @Comment("用户关注数")
	// @org.hibernate.annotations.ColumnDefault("0")
	// @Column(name="follow_num",insertable=false, nullable=false)
	// private Integer followNum;
	//
	// @Comment("用户回答数")
	// @org.hibernate.annotations.ColumnDefault("0")
	// @Column(name="answer_num",insertable=false, nullable=false)
	// private Integer answerNum;

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
