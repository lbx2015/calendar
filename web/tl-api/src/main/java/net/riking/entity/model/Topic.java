package net.riking.entity.model;

import java.util.Date;

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
@Comment("话题信息 表")
@Entity
@Table(name = "t_topic")
public class Topic extends BaseAuditProp {

	private static final long serialVersionUID = -4138765413765191901L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("topicId")
	private String id;

	@Comment("标题")
	@Column(name = "title", length = 255, nullable = false)
	private String title;

	@Comment("内容")
	@Lob
	@Column(name = "content", nullable = false)
	private String content;

	@Comment("话题url")
	@Lob
	@Column(name = "topic_url", nullable = false)
	private String topicUrl;

	@Transient
	@Comment("创建人名称")
	private String userName;

	@Transient
	@Comment("关注数")
	private Integer followNum;

	@Transient
	@Comment("用户头像")
	private String photoUrl;

	@Transient
	@Comment("是否已关注 0-未关注，1-已关注")
	private Integer isFollow;

	public Topic(String id, Date createdTime, Date modifiedTime, Integer isAudit, String title, String content,
			String topicUrl) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.setIsAudit(isAudit);
		this.title = title;
		this.content = content;
		this.topicUrl = topicUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Integer getFollowNum() {
		return followNum;
	}

	public void setFollowNum(Integer followNum) {
		this.followNum = followNum;
	}

	public String getTopicUrl() {
		return topicUrl;
	}

	public void setTopicUrl(String topicUrl) {
		this.topicUrl = topicUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
