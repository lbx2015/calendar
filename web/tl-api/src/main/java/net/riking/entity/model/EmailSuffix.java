package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;
import net.riking.entity.MyDateFormat;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("邮箱后缀表")
@Entity
@Table(name = "t_email_suffix")
public class EmailSuffix extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3940163346158751094L;

	@Id
	@GeneratedValue(generator = "idGenerator")
	@GenericGenerator(name = "idGenerator", strategy = "assigned")
	@Column(name = "email_suffix", length = 20)
	@Comment("邮箱后缀")
	private String emailSuffix;

	@Comment("用户状态 0-禁用 1-启用")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "enabled", nullable = false, precision = 1)
	private Integer enabled;

	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@MyDateFormat(pattern = "yyyyMMddHHmmssSSS")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;

	@Comment("是否删除： 0-删除，1-未删除")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "is_deleted", insertable = false, nullable = false, precision = 1)
	private Integer isDeleted;

	@Comment("备注")
	@Column(name = "remark", length = 255)
	private String remark;// 备注

	public String getEmailSuffix() {
		return emailSuffix;
	}

	public void setEmailSuffix(String emailSuffix) {
		this.emailSuffix = emailSuffix;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
