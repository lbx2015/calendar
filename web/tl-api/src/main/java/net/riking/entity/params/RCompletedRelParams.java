package net.riking.entity.params;

import java.util.Date;

import net.riking.entity.BaseEntity;

/**
 * 核销信息的接收参数
 * 
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class RCompletedRelParams extends BaseEntity {

	// 用户Id
	private String userId;
	// 报表id
	private String reportId;
	// 提醒时间（yyyyMMdd）
	private Date remindTime;
	//是否完成：0-未完成；1-完成
	private int isCompleted;
	//上报开始时间yyyyMMddHHmm
	private String submitStartTime;
	//上报结束时间yyyyMMddHHmm
	private String submitEndTime;
	//报表完成时间yyyyMMddHHmm
	private String completedDate;
	//闹钟提醒Id
	private String remindId;
	//当前选择的日期yyyyMMdd
	private String currentDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public Date getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public int getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(int isCompleted) {
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

	public String getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}

	public String getRemindId() {
		return remindId;
	}

	public void setRemindId(String remindId) {
		this.remindId = remindId;
	}
	
}
