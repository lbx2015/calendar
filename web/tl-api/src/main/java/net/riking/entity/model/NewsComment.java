package net.riking.entity.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
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
@Comment("行业资讯的评论表")
@Entity
@Table(name = "t_news_comment")
public class NewsComment extends BaseAuditProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8066495971201081735L;

	@Comment("物理主键")
	@Id
	@Column(name = "id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("newsCommentId")
	private String id;

	@Comment("操作人主键 : fk t_app_user 发表评论的用户id")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Comment("目标对象主键: fk t_news 行业资讯id")
	@Column(name = "news_id", nullable = false)
	private String newsId;

	@Comment("内容")
	@Column(name = "content", nullable = false)
	private String content;

	// 用户名称
	@Transient
	private String userName;

	// 点赞数
	@Transient
	private Integer agreeNumber;

	// 用户头像Url
	@Transient
	private String photoUrl;

	// 用户经验值
	@Transient
	private Integer experience;

	// 等级
	@Transient
	private Integer grade;

	@Transient
	@Comment("是否已点赞 0-未点赞，1-已点赞")
	private Integer isAgree;

	// 评论的回复list
	@Transient
	List<NCReply> ncReplyList;

	public NewsComment(String id, Date createdTime, Date modifiedTime, String userId, String newsId, String content,
			String userName, String photoUrl, Integer experience, String ncId) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.userId = userId;
		this.newsId = newsId;
		this.content = content;
		this.userName = userName;
		this.photoUrl = photoUrl;
		this.experience = experience;
		if (StringUtils.isNotBlank(ncId)) {
			isAgree = 1;
		} else {
			isAgree = 0;
		}
	}

	public NewsComment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<NCReply> getNcReplyList() {
		return ncReplyList;
	}

	public void setNcReplyList(List<NCReply> ncReplyList) {
		this.ncReplyList = ncReplyList;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public Integer getAgreeNumber() {
		return agreeNumber;
	}

	public void setAgreeNumber(Integer agreeNumber) {
		this.agreeNumber = agreeNumber;
	}

	public Integer getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Integer isAgree) {
		this.isAgree = isAgree;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
