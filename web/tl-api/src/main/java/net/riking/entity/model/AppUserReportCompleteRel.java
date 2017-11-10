package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

import net.riking.core.entity.PageQuery;
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
public class AppUserReportCompleteRel extends PageQuery {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column(name = "id", length = 17)
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

	// 同步标识app端数据状态
	@Column(name = "is_complete")
	private Integer isComplete; //是否完成（0-未完成；1-已完成）

	private String reportName;//报表名称
	
	private String strFrequency;//频度
	
	public AppUserReportCompleteRel(String id,String appUserId,String reportId,String completeDate,Integer isComplete,String reportName,String strFrenquency){
		this.id = id;
		this.appUserId = appUserId;
		this.reportId = reportId;
		this.completeDate = completeDate;
		this.isComplete = isComplete;
		this.reportName = reportName;
		this.strFrequency = strFrenquency;
	}
	
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

}
