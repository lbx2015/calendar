package net.riking.entity.model;

public class ReportFrequency {
	
	private String reportId;
	
	private String reportName;
	
	private String reportTitle;
	
	private String isComplete;
	
	private String strFrency;
	
	private String isSubscribe;//用户是否订阅
	
	public ReportFrequency(String reportId,String reportName,String reportTitle,String isComplete,String strFrency){
		this.reportId = reportId;
		this.reportName = reportName;
		this.reportTitle = reportTitle;
		this.isComplete = isComplete;
		this.strFrency = strFrency;
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
