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

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

@Comment("用户报表")
@Entity
@Table(name = "T_AppUser_Report_Rel")
public class AppUserReportRel extends BaseEntity {

	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// 用户ID
	@Column(name = "appUser_Id", length = 32)
	private String appUserId;
	// 报表ID
	@Column(name = "report_Id", length = 32)
	private String reportId;
	// 启用状态
	@Column(name = "enabled", length = 2)
	private String enabled;
	// 删除状态    0删除 1显示
	@Column(name = "delete_state", length = 2)
	private String deleteState;
	// 生效日期
	@Temporal(TemporalType.DATE)
	@Column(name = "startTime")
	private Date startTime;
	// 失效日期
	@Temporal(TemporalType.DATE)
	@Column(name = "overTime")
	private Date overTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(String appUserId) {
		this.appUserId = appUserId;
	}

	public String getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(String deleteState) {
		this.deleteState = deleteState;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	@Override
	public String toString() {
		return "AppUserReport [id=" + id + ", appUserId=" + appUserId + ", reportId=" + reportId + ", enabled="
				+ enabled + ", startTime=" + startTime + ", overTime=" + overTime + "]";
	}

}
