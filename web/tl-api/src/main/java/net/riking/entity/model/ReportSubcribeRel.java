package net.riking.entity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("用户报表订阅表")
@Entity
@IdClass(RSRelUnionPkId.class)
@Table(name = "t_report_subscribe_rel")
public class ReportSubcribeRel extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2353538414992037256L;

	public ReportSubcribeRel(String reportId) {
		super();
		this.reportId = reportId;
	}

	public ReportSubcribeRel() {
	}

	@Id
	@Comment("用户ID: fk t_app_user")
	@Column(name = "user_Id", length = 32)
	private String userId;

	@Id
	@Comment("报表ID: fk t_app_user")
	@Column(name = "report_id", length = 32)
	private String reportId;

	@Comment("是否完成核销：0-未完成；1-已完成")
	@Column(name = "is_complete", length = 32)
	private Integer isComplete;// 0：未完成；1：已完成

	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;

	public ReportSubcribeRel(String userId, String reportId, Integer isComplete, Date createdTime) {
		super();
		this.userId = userId;
		this.reportId = reportId;
		this.isComplete = isComplete;
		this.createdTime = createdTime;
	}

	public ReportSubcribeRel(String reportId, String reportName) {
		super();
		this.reportId = reportId;
		this.reportName = reportName;
	}

	@Transient
	private String reportName;// 报表名称

	@Transient
	private String type;

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
