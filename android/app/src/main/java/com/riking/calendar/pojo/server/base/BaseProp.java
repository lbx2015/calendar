package com.riking.calendar.pojo.server.base;

import java.util.Date;


public class BaseProp  {

//	@Comment("物理主键")
//	@Id
//	@Column(name = "id", length = 32)
//	@GenericGenerator(name = "system-uuid", strategy = "uuid")
//	@GeneratedValue(generator = "system-uuid")
//	public String id;

//	@Comment("创建人ID")
//	@Column(name = "created_by", updatable = false)
	public String createdBy;

//	@Comment("修改人ID")
//	@Column(name = "modified_by")
	public String modifiedBy;

//	@Comment("创建时间")
//	@Temporal(TemporalType.TIMESTAMP)
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
//	@org.hibernate.annotations.CreationTimestamp
//	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	public Date createdTime;

//	@Comment("修改时间")
//	@Temporal(TemporalType.TIMESTAMP)
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
//	@org.hibernate.annotations.UpdateTimestamp
//	@Column(name = "modified_time", insertable = false, nullable = false, columnDefinition = "datetime default now()")
	public Date modifiedTime;

//	@Comment("是否删除： 0-删除，1-未删除")
//	@org.hibernate.annotations.ColumnDefault("1")
//	@Column(name = "is_deleted", insertable = false, nullable = false, precision = 1)
	public Integer isDeleted;

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

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

}
