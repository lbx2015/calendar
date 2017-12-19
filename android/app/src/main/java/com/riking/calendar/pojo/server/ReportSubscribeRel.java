package com.riking.calendar.pojo.server;

import java.util.Date;

/**
 * @see
 * @since 1.0
 */
//@Comment("用户报表订阅表")
//@Entity
//@IdClass(RSRelUnionPkId.class)
//@Table(name = "t_report_subscribe_rel")
public class ReportSubscribeRel {

    //	@Id
//	@Comment("用户ID: fk t_app_user")
//	@Column(name = "user_id", length = 32)
    public String userId;

    //	@Id
//	@Comment("报表ID: fk t_app_user")
//	@Column(name = "report_id", length = 32)
    public String reportId;

    //	@Comment("报表code")
//	@Transient
    public String reportCode;

    /*@Comment("是否完成核销：0-未完成；1-已完成")
    @Column(name = "is_complete", length = 32)
    public Integer isComplete;// 0：未完成；1：已完成
*/
//	@Comment("创建时间")
//	@Temporal(TemporalType.TIMESTAMP)
//	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
//	@JsonFormat(pattern = "yyyyMMddHHmmssSSS")
//	@org.hibernate.annotations.CreationTimestamp
//	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
    public Date createdTime;
    //	@Transient
    public String reportName;// 报表名称
    //	@Transient
    public String type;

}
