package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;

/**
 * 
 * @author tao.yuan
 * @version crateTime：2017年11月15日 下午3:23:53
 * @used 评论回答
 */
@Entity
@Table(name = "t_answer_comment")
public class AnswerComment {
	
	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;
	
	@Comment("回答的问题id")
	@Column(name = "answer_id", length = 32)
	private String anwerId;//回答的问题id
	
	@Comment("评论的用户id")
	@Column(name = "user_id", length = 32)
	private String userId;//评论的用户id
	
	@Comment("被评论的用户id")
	@Column(name = "to_user_id", length = 64)
	private Long toUserId;//被评论的用户id
	
	@Comment("评论内容")
	@Column(name = "comment_content", length = 512)
	private String commentContent;//评论内容
	
	@Comment("回答问题的时间")
	@Column(name = "create_time", length = 64)
	private String createTime;//回答问题的时间


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnwerId() {
		return anwerId;
	}

	public void setAnwerId(String anwerId) {
		this.anwerId = anwerId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getToUserId() {
		return toUserId;
	}

	public void setToUserId(Long toUserId) {
		this.toUserId = toUserId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
//	@Comment("总评论数")
//	@Column(name = "sum_comment", length = 16)
//	private String sumComment;//总评论数
	
//	@Comment("回答这个问题的总点赞数")
//	@Column(name = "sum_praise", length = 16)
//	private String sumPraise;//总点赞数

}
