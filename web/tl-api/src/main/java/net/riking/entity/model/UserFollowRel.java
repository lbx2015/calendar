package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("关注用户 表")
@Entity
@IdClass(UFollowRelUnionPkId.class)
@Table(name = "t_user_follow_rel")
public class UserFollowRel extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2048938865811370457L;

	@Id
	@Comment("fk t_app_user 关注的用户ID")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Id
	@Comment("fk t_app_user 被关注的用户ID")
	@Column(name = "to_user_id", nullable = false)
	private String toUserId;

	@Comment("关注状态: 0：非互相关注 1：互相关注 ")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "follow_status", nullable = false, precision = 1)
	private Integer followStatus;

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

	public Integer getFollowStatus() {
		return followStatus;
	}

	public void setFollowStatus(Integer followStatus) {
		this.followStatus = followStatus;
	}

}
