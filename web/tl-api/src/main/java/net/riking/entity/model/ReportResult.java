package net.riking.entity.model;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnore;

import net.riking.core.annos.Comment;

public class ReportResult implements Serializable {
	
	@JsonIgnore
	@Comment("报表类型")
	private String reportType;
	
	@JsonIgnore
	@Comment("报表类型中文名称")
	private String reportTypeName;
	
	@Comment("报表种类")
	private String reportKind;
	
	@Comment("报表种类名称")
	private String reportKindName;
	
	@Comment("报表所属模块")
	private String moduleType;
	
	@Comment("报表所属模块名称")
	private String moduleTypeName;
	
	@Comment("报表Id")
	private String reportId;
	
	@Comment("报表代码")
	private String code;
	
	@Comment("报表标题")
	private String title;
	
	@Comment("是否订阅")
	private String isSubcribe;

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getReportTypeName() {
		return reportTypeName;
	}

	public void setReportTypeName(String reportTypeName) {
		this.reportTypeName = reportTypeName;
	}

	public String getReportKind() {
		return reportKind;
	}

	public void setReportKind(String reportKind) {
		this.reportKind = reportKind;
	}

	public String getReportKindName() {
		return reportKindName;
	}

	public void setReportKindName(String reportKindName) {
		this.reportKindName = reportKindName;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getModuleTypeName() {
		return moduleTypeName;
	}

	public void setModuleTypeName(String moduleTypeName) {
		this.moduleTypeName = moduleTypeName;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
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

	public String getIsSubcribe() {
		return isSubcribe;
	}

	public void setIsSubcribe(String isSubcribe) {
		this.isSubcribe = isSubcribe;
	}
	
}
