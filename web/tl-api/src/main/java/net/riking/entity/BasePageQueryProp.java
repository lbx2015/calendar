package net.riking.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import net.riking.core.annos.Comment;

/***
 * 有分页的基类
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@MappedSuperclass
public class BasePageQueryProp extends PageQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8495148802285901698L;

	@Comment("创建人ID")
	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@Comment("修改人ID")
	@Column(name = "modified_by")
	private String modifiedBy;

	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;

	@Comment("修改时间")
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.UpdateTimestamp
	@Column(name = "modified_time", insertable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date modifiedTime;

	@Comment("是否审核： 0-未审核，1-已审核")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "is_aduit", nullable = false, precision = 1)
	private Integer isAduit;

	@Comment("是否启用：0-禁用；1-启用")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "enabled", length = 1)
	private Integer enabled;

	@Comment("是否删除： 0-删除，1-未删除")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "is_deleted", insertable = false, nullable = false, precision = 1)
	private Integer isDeleted;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Integer getIsAduit() {
		return isAduit;
	}

	public void setIsAduit(Integer isAduit) {
		this.isAduit = isAduit;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

}
