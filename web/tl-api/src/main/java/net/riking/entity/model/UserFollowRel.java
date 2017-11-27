package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import net.riking.core.annos.Comment;

@Comment("关注用户 表")
@Entity
@Table(name = "t_user_follow_rel")
public class UserFollowRel {

	private static final long serialVersionUID = 4125640911685324258L;

	@Comment("fk t_app_user 关注的用户ID")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Comment("fk t_app_user 被关注的用户ID")
	@Column(name = "to_user_id", nullable = false)
	private String toUserId;

	@Comment("关注状态: 0：非互相关注 1：互相关注 ")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "follow_status", insertable = false, nullable = false, precision = 1)
	private Integer follow_status;

	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;

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

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getFollow_status() {
		return follow_status;
	}

	public void setFollow_status(Integer follow_status) {
		this.follow_status = follow_status;
	}

}
