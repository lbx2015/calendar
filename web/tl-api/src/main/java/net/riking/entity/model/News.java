package net.riking.entity.model;

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

	@Comment("资讯标题")
	@Column(name = "title", length = 128)
	private String title;

	@Comment("封面位置：right；center")
	@Lob
	@org.hibernate.annotations.ColumnDefault("center")
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

	// 评论数
	@Transient
	private Integer commentNumber;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getCommentNumber() {
		return commentNumber;
	}

	public void setCommentNumber(Integer commentNumber) {
		this.commentNumber = commentNumber;
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
