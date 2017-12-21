package net.riking.entity.VO;

import net.riking.entity.model.Report;
import net.riking.entity.model.ReportSubmitCaliber;

/**
 * WEB端：report VO对象
 * 
 * @author fu.chen
 *
 */

public class ReportVO {
	private String id;
	private Report report;
	private ReportSubmitCaliber reportSubmitCaliber;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}

	public ReportSubmitCaliber getReportSubmitCaliber() {
		return reportSubmitCaliber;
	}

	public void setReportSubmitCaliber(ReportSubmitCaliber reportSubmitCaliber) {
		this.reportSubmitCaliber = reportSubmitCaliber;
	}

}
