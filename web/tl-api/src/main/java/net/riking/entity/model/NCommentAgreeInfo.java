package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("资讯评论点赞信息 表")
@Entity
@Table(name = "t_ncomment_agree_info")
public class NCommentAgreeInfo extends BaseProp {

	private static final long serialVersionUID = -2001660462476390821L;
	
	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Comment("目标对象主键")
	@Column(name = "news_comment_id", nullable = false)
	private String newsCommentId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNewsCommentId() {
		return newsCommentId;
	}

	public void setNewsCommentId(String newsCommentId) {
		this.newsCommentId = newsCommentId;
	}
	
	
}
