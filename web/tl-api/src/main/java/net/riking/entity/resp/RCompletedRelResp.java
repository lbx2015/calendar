package net.riking.entity.resp;

import net.riking.entity.BaseEntity;

/**
 * 核销表返回参数
 * @author jc.tan 2017年11月29日
 * @see
 * @since 1.0
 */
public class RCompletedRelResp extends BaseEntity {

	private String reportId;

	private String userId;

	private String completedDate;

	private String reportName;

	private String frequency;

	public RCompletedRelResp(String reportId) {
		super();
		this.reportId = reportId;
	}

	public RCompletedRelResp(String userId, String reportId, String completedDate, String reportName,
			String frequency) {
		super();
		this.userId = userId;
		this.reportId = reportId;
		this.completedDate = completedDate;
		this.reportName = reportName;
		this.frequency = frequency;
	}

	public RCompletedRelResp() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(String completedDate) {
		this.completedDate = completedDate;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

}
