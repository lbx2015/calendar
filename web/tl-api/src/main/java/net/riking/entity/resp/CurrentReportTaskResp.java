package net.riking.entity.resp;

import net.riking.entity.BaseEntity;

/**
 * 工作台当前报表核销任务
 * @author james.you
 * @version crateTime：2017年12月16日 下午5:37:01
 * @used TODO
 */
public class CurrentReportTaskResp extends BaseEntity {
	private String reportId;

	private String reportCode;

	private String reportName;// 报表名称

	private Integer frequency;// 报表频度

	private String reportBatch;// 批次0,1,2,3,4, 其它

	private String submitStartTime;// 上报开始时间yyyyMMdd

	private String submitEndTime;// 上报结束时间yyyyMMdd

	private String isCompleted;// 是否完成：0-未完成；1-完成

	private String remindId;// 提醒记录Id

	private String remindContent;// 提醒的报表内容

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
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

	public String getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(String isCompleted) {
		this.isCompleted = isCompleted;
	}

	public String getRemindId() {
		return remindId;
	}

	public void setRemindId(String remindId) {
		this.remindId = remindId;
	}

	public String getRemindContent() {
		return remindContent;
	}

	public void setRemindContent(String remindContent) {
		this.remindContent = remindContent;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getReportBatch() {
		return reportBatch;
	}

	public void setReportBatch(String reportBatch) {
		this.reportBatch = reportBatch;
	}

}
