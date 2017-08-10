package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import net.riking.core.annos.Comment;
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
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// 用户ID"
	@Column(name = "app_user_id", length = 32)
	private String appUserId;

	// 报表ID"
	@Column(name = "report_id", length = 32)
	private String reportId;

	// 报表完成时间（yyyyMMdd）
	@Column(name = "complete_date", length = 8)
	private String completeDate;

	// 是否完成（0-未完成；1-已完成）
	@Column(name = "is_complete", length = 1)
	private Integer isComplete;

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
