package com.riking.calendar.pojo.server;

public class ReportFrequency {
	public String reportId;
	public String reportName;
	public String reportTitle;
	public String isComplete;
	public String strFrency;
	public ReportFrequency(String reportId,String reportName,String reportTitle,String isComplete,String strFrency){
		this.reportId = reportId;
		this.reportName = reportName;
		this.reportTitle = reportTitle;
		this.isComplete = isComplete;
		this.strFrency = strFrency;
	}

}
