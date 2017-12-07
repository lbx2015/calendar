package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

public class ReportFrequency extends BaseEntity {

	private String reportId;

	private String reportCode;

	private String reportTitle;

	private String isComplete;

	private String strFrency;

	private String isSubscribe;// 用户是否订阅

	public ReportFrequency(String reportId, String reportCode, String reportTitle, String isComplete,
			String strFrency) {
		this.reportId = reportId;
		this.reportCode = reportCode;
		this.reportTitle = reportTitle;
		this.isComplete = isComplete;
		this.strFrency = strFrency;
	}

	public ReportFrequency(String reportId, String reportCode) {
		super();
		this.reportId = reportId;
		this.reportCode = reportCode;
	}

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

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public String getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(String isComplete) {
		this.isComplete = isComplete;
	}

	public String getStrFrency() {
		return strFrency;
	}

	public void setStrFrency(String strFrency) {
		this.strFrency = strFrency;
	}

	public String getIsSubscribe() {
		return isSubscribe;
	}

	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}

}
