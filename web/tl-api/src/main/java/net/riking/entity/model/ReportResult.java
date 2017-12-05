package net.riking.entity.model;

import java.io.Serializable;

import net.riking.core.annos.Comment;

public class ReportResult implements Serializable {
	
	@Comment("机构")
	private String agenceCode;
	
	@Comment("报表类型")
	private String reportType;
	
	@Comment("报表模式分类")
	private String reportMode;
	
	@Comment("报表代码")
	private String code;
	
	@Comment("报表标题")
	private String title;
	
	@Comment("报表模式分类")
	private String reportId;
	
	@Comment("是否订阅")
	private String isSubcribe;


	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportMode() {
		return reportMode;
	}

	public void setReportMode(String reportMode) {
		this.reportMode = reportMode;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getIsSubcribe() {
		return isSubcribe;
	}

	public void setIsSubcribe(String isSubcribe) {
		this.isSubcribe = isSubcribe;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setAgenceCode(String agenceCode) {
		this.agenceCode = agenceCode;
	}

	public String getAgenceCode() {
		return agenceCode;
	}
	
	
}
