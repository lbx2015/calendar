package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseProp;

@Comment("关注用户 表")
@Entity
@Table(name = "t_user_flow_info")
public class UserFlowInfo extends BaseProp{
	
	private static final long serialVersionUID = 4125640911685324258L;

	@Comment("关注发起人主键  ")
	@Column(name = "user_id", nullable = false)
	private String  userId;
	
	@Comment("被关注人主键  ")
	@Column(name = "to_user_id", nullable = false)
	private String toUserId;
	
	@Comment("是否互相关注   0： 否，1：是 ")
	@org.hibernate.annotations.ColumnDefault("0")  
	@Column(name="flow_state",insertable=false, nullable=false, precision=1)
	private Integer flowState;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public Integer getFlowState() {
		return flowState;
	}

	public void setFlowState(Integer flowState) {
		this.flowState = flowState;
	}
	
	
}
