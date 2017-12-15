package net.riking.entity.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 8785572228225541248L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("topicQuestionId")
	private String id;

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

	// 用户头像Url
	@Transient
	private String photoUrl;

	// 用户关注数
	@Transient
	private Integer followNum;

	// 用户回答数
	@Transient
	private Integer answerNum;

	// 经验值
	@Transient
	private Integer experience;

	// 等级
	@Transient
	private Integer grade;

	@Transient
	@Comment("是否已关注 0-未关注，1-已关注")
	private Integer isFollow;

	@Transient
	private List<QuestionAnswer> questionAnswers;

	public TopicQuestion(String id, Date createdTime, Date modifiedTime, Integer isAudit, String title, String content,
			String topicId, String userId, String userName, String photoUrl, Integer experience) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.setIsAudit(isAudit);
		this.title = title;
		this.content = content;
		this.topicId = topicId;
		this.userId = userId;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.experience = experience;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<QuestionAnswer> getQuestionAnswers() {
		return questionAnswers;
	}

	public void setQuestionAnswers(List<QuestionAnswer> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Integer getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(Integer isFollow) {
		this.isFollow = isFollow;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getFollowNum() {
		return followNum;
	}

	public void setFollowNum(Integer followNum) {
		this.followNum = followNum;
	}

	public Integer getAnswerNum() {
		return answerNum;
	}

	public void setAnswerNum(Integer answerNum) {
		this.answerNum = answerNum;
	}

}
