package net.riking.entity.model;

public class QueryReport {
	private String id;
	private String reportName;
	private String reportCode;
	private String moduleType;
	public QueryReport(){}
	
	public QueryReport(String id, String reportName, String reportCode, String moduleType) {
		super();
		this.id = id;
		this.reportName = reportName;
		this.reportCode = reportCode;
		this.moduleType = moduleType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

}
