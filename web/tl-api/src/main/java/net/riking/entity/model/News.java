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
@Comment("行业资讯表")
@Entity
@Table(name = "t_news")
public class News extends BaseAuditProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = -766591260385281272L;

	@Comment("资讯标题")
	@Column(name = "title", length = 128)
	private String title;

	@Comment("封面位置：right；center")
	@Lob
	@Column(name = "seat", length = 10)
	private String seat;

	@Comment("多个封面URL，封号分隔")
	@Lob
	@Column(name = "cover_urls", length = 255)
	private String coverUrls;

	@Comment("资讯内容")
	@Lob
	@Column(name = "content")
	private String content;

	@Comment("发布单位")
	@Column(name = "issued", length = 255, nullable = false)
	private String issued;

	public News(String id, Date createdTime, Date modifiedTime, String title, String seat, String coverUrls,
			String content, String issued, String userName, String photoUrl) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.title = title;
		this.seat = seat;
		this.coverUrls = coverUrls;
		this.content = content;
		this.issued = issued;
		this.userName = userName;
		this.photoUrl = photoUrl;
	}

	public News(String id, Date createdTime, Date modifiedTime, String title, String seat, String coverUrls,
			String content, String issued) {
		super();
		this.setId(id);
		this.setCreatedTime(createdTime);
		this.setModifiedTime(modifiedTime);
		this.title = title;
		this.seat = seat;
		this.coverUrls = coverUrls;
		this.content = content;
		this.issued = issued;
	}

	// 用户名
	@Transient
	private String userName;

	// 评论数
	@Transient
	private Integer commentNumber;

	// 用户头像Url
	@Transient
	private String photoUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public Integer getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public String getCoverUrls() {
		return coverUrls;
	}

	public void setCoverUrls(String coverUrls) {
		this.coverUrls = coverUrls;
	}

	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}

}
