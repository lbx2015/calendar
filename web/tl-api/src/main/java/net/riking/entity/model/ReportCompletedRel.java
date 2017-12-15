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
import net.riking.core.entity.PageQuery;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("用户核销表")
@Entity
@IdClass(RCRelUnionPkId.class)
@Table(name = "t_report_completed_rel")
public class ReportCompletedRel extends PageQuery {

	@Id
	@Comment("用户ID")
	@Column(name = "user_id", length = 32)
	private String userId;

	@Id
	@Comment("报表ID")
	@Column(name = "report_id", length = 32)
	private String reportId;
	
	@Comment("是否完成（0-未完成；1-已完成）")
	@Column(name = "is_completed", length = 8)
	private String isCompleted;

	@Comment("报表完成时间（yyyyMMdd）")
	@Column(name = "completed_date", length = 8)
	private String completedDate;
	
	@Comment("报送开始时间（yyyyMMddHHmm）")
	@Column(name = "submit_start_time", length = 8)
	private String submitStartTime;
	
	@Comment("报送截止时间（yyyyMMddHHmm）")
	@Column(name = "submit_end_time", length = 8)
	private String submitEndTime;
	
	@Comment("创建时间")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.S")
	@org.hibernate.annotations.CreationTimestamp
	@Column(name = "created_time", insertable = false, updatable = false, nullable = false, columnDefinition = "datetime default now()")
	private Date createdTime;

	@Transient
	private String reportCode;// 报表名称
	
	@Transient
	private String reportName;// 报表名称

	@Transient
	private String strFrequency;// 频度

	public ReportCompletedRel(String reportId) {
		super();
		this.reportId = reportId;
	}

	public ReportCompletedRel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReportCompletedRel(String userId, String reportId, String completedDate, String reportName,
			String strFrequency) {
		super();
		this.userId = userId;
		this.reportId = reportId;
		this.completedDate = completedDate;
		this.reportName = reportName;
		this.strFrequency = strFrequency;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

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

	public String getStrFrequency() {
		return strFrequency;
	}

	public void setStrFrequency(String strFrequency) {
		this.strFrequency = strFrequency;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}

	public String getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getSubmitStartTime() {
		return submitStartTime;
	}

	public void setSubmitStartTime(String submitStartTime) {
		this.submitStartTime = submitStartTime;
	}

	public String getSubmitEndTime() {
		return submitEndTime;
	}

	public void setSubmitEndTime(String submitEndTime) {
		this.submitEndTime = submitEndTime;
	}

}
