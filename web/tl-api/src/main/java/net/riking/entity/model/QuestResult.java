package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

public class QuestResult extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418351387803616527L;

	@Comment("物理主键")
	@JsonProperty("questionId")
	private String id;

	@Comment("标题")
	private String title;

	@Comment("内容")
	private String content;

	@Comment("创建时间")
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	private Date createdTime;

	// 问题关注数
	@Transient
	private Integer tqFollowNum;

	// 问题回答评论数
	@Transient
	private Integer qaCommentNum;

	// 用户名
	@Transient
	private String userName;

	// 用户头像Url
	@Transient
	private String photoUrl;

	public QuestResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuestResult(String id, String title, Date createdTime, Long tqFollowNum, Long qaCommentNum) {
		super();
		this.id = id;
		this.title = title;
		this.createdTime = createdTime;
		if (tqFollowNum != null) {
			this.tqFollowNum = tqFollowNum.intValue();
		}
		if (qaCommentNum != null) {
			this.qaCommentNum = qaCommentNum.intValue();
		}
	}

	public QuestResult(String id, String title) {
		super();
		this.id = id;
		this.title = title;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getTqFollowNum() {
		return tqFollowNum;
	}

	public void setTqFollowNum(Integer tqFollowNum) {
		this.tqFollowNum = tqFollowNum;
	}

	public Integer getQaCommentNum() {
		return qaCommentNum;
	}

	public void setQaCommentNum(Integer qaCommentNum) {
		this.qaCommentNum = qaCommentNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

}
