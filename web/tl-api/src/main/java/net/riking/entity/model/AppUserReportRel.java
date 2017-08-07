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

/**
 * @author Lucky.Liu on 2017/8/05.
 */

@Comment("用户报表")
@Entity
@Table(name = "T_appUser_report_rel")
public class AppUserReportRel extends BaseEntity {

	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// 用户ID
	@Column(name = "appUser_id", length = 32)
	private String appUserId;
	// 报表ID
	@Column(name = "report_id", length = 32)
	private String reportId;
	// 删除状态 0删除 1显示
	@Column(name = "delete_state", length = 2)
	private String deleteState;

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

	@Override
	public String toString() {
		return "AppUserReportRel [id=" + id + ", appUserId=" + appUserId + ", reportId=" + reportId + ", deleteState="
				+ deleteState + "]";
	}

}
