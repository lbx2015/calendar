package net.riking.entity.model;

import net.riking.core.entity.BaseEntity;

public class QueryReport extends BaseEntity {
	private String id;

	private String reportName;

	private String reportCode;

	private String moduleType;

	private String caliberType;

	private String frequency;

	private String toOrgan;// 汇报机构

	private String reportTypeName;// 汇报中文名称

	private String reportPacketName;// 汇报包结构中文名称

	private String strFrequency;// 频度

	public QueryReport() {
	}

	public QueryReport(String id, String reportName, String toOrgan, String reportTypeName, String reportPacketName,
			String strFrequency, String str) {
		super();
		this.id = id;
		this.reportName = reportName;
		this.toOrgan = toOrgan;
		this.reportTypeName = reportTypeName;
		this.reportPacketName = reportPacketName;
		this.strFrequency = strFrequency;
	}

	public QueryReport(String id, String reportName, String reportCode, String moduleType) {
		super();
		this.id = id;
		this.reportName = reportName;
		this.reportCode = reportCode;
		this.moduleType = moduleType;
	}

	public QueryReport(String id, String reportName, String reportCode, String moduleType, String caliberType,
			String frequency) {
		super();
		String bewteen = null;
		this.id = id;
		this.reportCode = reportCode;
		this.moduleType = moduleType;
		switch (caliberType) {
			case "1":
				bewteen = "标准";
				break;
			case "2":
				bewteen = "分支机构";
				break;
			case "3":
				bewteen = "法人";
				break;
			case "4":
				bewteen = "合并";
				break;
			case "5":
				bewteen = "其它";
				break;
			default:
				bewteen = "";
				break;
		}
		switch (frequency) {
			case "1":
				this.reportName = reportName + bewteen + "日报";
				break;
			case "2":
				this.reportName = reportName + bewteen + "周报";
				break;
			case "3":
				this.reportName = reportName + bewteen + "旬报";
				break;
			case "4":
				this.reportName = reportName + bewteen + "月报";
				break;
			case "5":
				this.reportName = reportName + bewteen + "季报";
				break;
			case "6":
				this.reportName = reportName + bewteen + "半年报";
				break;
			case "7":
				this.reportName = reportName + bewteen + "年报";
				break;
			default:
				this.reportName = reportName + bewteen + "";
				break;
		}
		this.caliberType = caliberType;
		this.frequency = frequency;
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

	public String getCaliberType() {
		return caliberType;
	}

	public void setCaliberType(String caliberType) {
		this.caliberType = caliberType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getToOrgan() {
		return toOrgan;
	}

	public void setToOrgan(String toOrgan) {
		this.toOrgan = toOrgan;
	}

	public String getReportTypeName() {
		return reportTypeName;
	}

	public void setReportTypeName(String reportTypeName) {
		this.reportTypeName = reportTypeName;
	}

	public String getReportPacketName() {
		return reportPacketName;
	}

	public void setReportPacketName(String reportPacketName) {
		this.reportPacketName = reportPacketName;
	}

	public String getStrFrequency() {
		return strFrequency;
	}

	public void setStrFrequency(String strFrequency) {
		this.strFrequency = strFrequency;
	}

}
