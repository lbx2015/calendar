package net.riking.entity.resp;

import javax.persistence.Transient;

/**
 * 报表核销结果集
 * @author james.you
 * @version crateTime：2017年11月29日 下午3:16:14
 * @used TODO
 */
public class ReportCompletedRelResult{
	@Transient
	private String reportCode;// 报表code
	
	@Transient
	private String reportName;// 报表名称

	@Transient
	private String frequencyType;// 频度类型
	
	@Transient
	private String dateStr;// 日期

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


	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

}
