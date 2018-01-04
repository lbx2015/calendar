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

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;
import net.riking.entity.MyDateFormat;

/**
 * 
 * @author james.you
 * @version crateTime：2018年1月3日 下午4:22:54
 * @used TODO
 */
@Comment("系统通知阅读表")
@Entity
@IdClass(SysNoticeReadUnionPkId.class)
@Table(name = "t_sys_notice_read")
public class SysNoticeRead extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Comment("fk t_notice")
	@Column(name = "notice_id", length = 32, nullable = false)
	private String noticeId;
	
	@Id
	@Comment("fk t_app_user")
	@Column(name = "user_id", length = 32, nullable = false)
	private String userId;
	
	@Comment("是否删除：1-未删除；0-删除（系统信息删除时，存入删除记录）")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "is_deleted", insertable = false, nullable = false, precision = 1)
	private Integer isDeleted;
	
	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@MyDateFormat(pattern = "yyyyMMddHHmmssSSS")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;
	
	public SysNoticeRead() {}

	public SysNoticeRead(String noticeId, String userId) {
		super();
		this.noticeId = noticeId;
		this.userId = userId;
	}

	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
