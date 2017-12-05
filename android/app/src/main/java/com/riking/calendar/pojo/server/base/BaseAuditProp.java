package com.riking.calendar.pojo.server.base;

import java.util.Date;

/***
 * 有审核的基类
 */
//@MappedSuperclass
public class BaseAuditProp {

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

    //	@Comment("是否审核： 0-未审核，1-已审核,2-不通过")
//	@org.hibernate.annotations.ColumnDefault("0")
//	@Column(name = "is_aduit", nullable = false, precision = 1)
    public Integer isAduit;

    //	@Comment("是否删除： 0-删除，1-未删除")
//	@org.hibernate.annotations.ColumnDefault("1")
//	@Column(name = "is_deleted", insertable = false, nullable = false, precision = 1)
    public Integer isDeleted;

}
