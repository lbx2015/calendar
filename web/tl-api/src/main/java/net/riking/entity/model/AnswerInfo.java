package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("回答信息 表")
@Entity
@Table(name = "t_answer_info")
public class AnswerInfo extends BaseProp {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3639987103489963199L;
	
	@Comment("回答人主键  ")
	@Column(name = "user_id", nullable = false)
	private String  userId;
	
	@Comment("问题主键  ")
	@Column(name = "question_id", nullable = false)
	private String questionId;
	
	@Comment("回答内容")
	@Lob
	@Column(name = "content", nullable = false)
	private String content;
	
	@Comment("用户收藏数")
	@org.hibernate.annotations.ColumnDefault("0")  
	@Column(name="collect_num",insertable=false, nullable=false)
	private Integer collectNum;
	
	@Comment("用户评论数")
	@org.hibernate.annotations.ColumnDefault("0")  
	@Column(name="comment_num",insertable=false, nullable=false)
	private Integer commentNum;
	
	@Comment("用户点赞数")
	@org.hibernate.annotations.ColumnDefault("0")  
	@Column(name="praise_num",insertable=false, nullable=false)
	private Integer praiseNum;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getCollectNum() {
		return collectNum;
	}

	public void setCollectNum(Integer collectNum) {
		this.collectNum = collectNum;
	}

	public Integer getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Integer commentNum) {
		this.commentNum = commentNum;
	}

	public Integer getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}
	

}
