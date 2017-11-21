package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("评论信息 表")
@Entity
@Table(name = "t_comment_info")
public class CommentInfo extends BaseProp{
	
	private static final long serialVersionUID = 954660049052687792L;

	@Comment("评论人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;
	
	@Comment("目标对象类型（0:回答, 1:评论 ）")
	@Column(name = "target_type", nullable = false)
	private Integer targetType;
	
	@Comment("目标对象主键")//（包括 回答 评论）
	@Column(name = "target_id", nullable = false)
	private String targetId;
	
	@Comment("被评论人主键  ")
	@Column(name = "to_user_id", nullable = false)
	private String toUserId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getTargetType() {
		return targetType;
	}

	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}
	
	
	
	
}
