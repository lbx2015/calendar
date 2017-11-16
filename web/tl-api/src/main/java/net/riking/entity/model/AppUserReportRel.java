package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

/**
 * Created by Lucky.Liu on 2017/8/05.
 */

@Comment("用户报表")
@Entity
@Table(name = "t_appuser_report_rel")
public class AppUserReportRel extends BaseEntity {

	
	public AppUserReportRel(String reportId) {
		super();
		this.reportId = reportId;
	}

	public AppUserReportRel() {
		// TODO Auto-generated constructor stub
	}
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
	
	// 是否完成
	@Column(name = "is_complete", length = 32)
	private String isComplete;//0：未完成；1：已完成

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

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	@Override
	public String toString() {
		return "AppUserReportRel [id=" + id + ", appUserId=" + appUserId + ", reportId=" + reportId + ", isComplete="
				+ isComplete + "]";
	}

}
