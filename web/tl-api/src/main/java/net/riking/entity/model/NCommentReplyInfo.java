package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("资讯评论回复信息 表")
@Entity
@Table(name = "t_ncomment_reply_info")
public class NCommentReplyInfo extends BaseProp{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5652341254448442067L;

	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Comment("被操作人主键  ")
	@Column(name = "to_user_id", nullable = false)
	private String toUserId;
	
	@Comment("目标对象评论主键")
	@Column(name = "news_comment_id", nullable = false)
	private String newsCommentId;
	
	@Comment("目标对象评论回复主键")
	@Column(name = "n_comment_reply_id", nullable = false)
	private String nCommentReplyId;
	
	@Comment("内容")
	@Column(name = "content", nullable = false)
	private String content;

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

	public String getnCommentReplyId() {
		return nCommentReplyId;
	}

	public void setnCommentReplyId(String nCommentReplyId) {
		this.nCommentReplyId = nCommentReplyId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	
	
}
