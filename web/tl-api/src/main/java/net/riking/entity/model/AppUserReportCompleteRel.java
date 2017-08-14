package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import net.riking.entity.BaseEntity;

/**
 * 记录用户每天报表完成的状态
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月10日 下午7:01:46
 * @used TODO
 */
@Entity
@Table(name = "t_app_user_report_complete_rel")
public class AppUserReportCompleteRel extends BaseEntity {

	@Id
	@Column(name = "complete_id", length = 17)
	private String completeId;

	// 用户ID"
	@Column(name = "app_user_id", length = 32)
	private String appUserId;

	// 报表ID"
	@Column(name = "report_id", length = 32)
	private String reportId;

	// 报表完成时间（yyyyMMdd）
	@Column(name = "complete_date", length = 8)
	private String completeDate;

	// 同步标识app端数据状态
	@Transient
	private Integer isComplete; 

	public String getCompleteId() {
		return completeId;
	}

	public void setCompleteId(String completeId) {
		this.completeId = completeId;
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

	public String getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(String completeDate) {
		this.completeDate = completeDate;
	}

	public Integer getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Integer isComplete) {
		this.isComplete = isComplete;
	}

}
