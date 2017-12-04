package net.riking.entity.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Comment("行业资讯的评论表")
@Entity
@Table(name = "t_news_comment")
public class NewsComment extends BaseAuditProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8066495971201081735L;

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

	// 评论的回复list
	@Transient
	List<Map<String, Object>> nCommentReplyInfoList;

	public NewsComment(String id, Date createdTime, Date modifiedTime, String userId, String newsId, String content,
			String userName, String photoUrl) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.userId = userId;
		this.newsId = newsId;
		this.content = content;
		this.userName = userName;
		this.photoUrl = photoUrl;
	}

	public NewsComment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<Map<String, Object>> getNCommentReplyInfoList() {
		if (nCommentReplyInfoList == null) {
			nCommentReplyInfoList = new ArrayList<Map<String, Object>>();
		}
		return this.nCommentReplyInfoList;
	}

	public String getUserId() {
		return userId;
	}

	public Integer getAgreeNumber() {
		return agreeNumber;
	}

	public void setAgreeNumber(Integer agreeNumber) {
		this.agreeNumber = agreeNumber;
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
