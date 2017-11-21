package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("操作信息 表")
@Entity
@Table(name = "t_operation_info")
public class OperationInfo extends BaseProp{

	/**
	 * 
	 */
	private static final long serialVersionUID = -625804863730214269L;
	
	
	@Comment("操作类型（0：关注，1：收藏，3：点赞，4：屏蔽）")
	@Column(name = "type", nullable = false)
	private Integer type;
	
	@Comment("目标对象类型（0:话题, 1:问题 , 2: 回答 ）")
	@Column(name = "target_type", nullable = false)
	private Integer targetType;
	
	@Comment("目标对象主键")//（包括 话题 问题 回答 ）
	@Column(name = "target_id", nullable = false)
	private String targetId;
	
	@Comment("目标对象所属上级主键  ")//（包括 话题、问题）
	@Column(name = "parent_id", nullable = false)
	private String parentId;
	
	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
