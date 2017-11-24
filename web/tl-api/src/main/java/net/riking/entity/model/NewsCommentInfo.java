package net.riking.entity.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("资讯评论信息 表")
@Entity
@Table(name = "t_news_comment_info")
public class NewsCommentInfo extends BaseProp {

	private static final long serialVersionUID = 2480706725001723561L;

	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Comment("目标对象主键")
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

	// 评论的回复list
	@Transient
	List<Map<String, Object>> nCommentReplyInfoList;

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
