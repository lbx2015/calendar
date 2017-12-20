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

/**
 * 通讯录邀请表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("通讯录邀请表")
@Entity
@IdClass(ContactsInviteUnionPkId.class)
@Table(name = "t_contacts_invite")
public class ContactsInvite extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5370380117246181873L;

	public ContactsInvite() {
		super();
	}

	@Id
	@Comment("操作人主键  ")
	@Column(name = "user_id", nullable = false)
	private String userId;

	@Id
	@Comment("手机号")
	@Column(name = "phone", length = 11, nullable = false)
	private String phone;

	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
