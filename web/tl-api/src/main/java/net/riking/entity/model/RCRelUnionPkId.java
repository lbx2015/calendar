package net.riking.entity.model;

import net.riking.core.entity.PageQuery;

/**
 * 用户核销表复合主键表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class RCRelUnionPkId extends PageQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = 330194607996518109L;

	private String userId;
	private String reportId;
	private String submitStartTime;
	private String submitEndTime;
	
	public RCRelUnionPkId() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RCRelUnionPkId(String userId, String reportId, String submitStartTime, String submitEndTime) {
		super();
		this.userId = userId;
		this.reportId = reportId;
		this.submitStartTime = submitStartTime;
		this.submitEndTime = submitEndTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reportId == null) ? 0 : reportId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RCRelUnionPkId other = (RCRelUnionPkId) obj;
		if (reportId == null) {
			if (other.reportId != null)
				return false;
		} else if (!reportId.equals(other.reportId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	public String getSubmitStartTime() {
		return submitStartTime;
	}

	public void setSubmitStartTime(String submitStartTime) {
		this.submitStartTime = submitStartTime;
	}

	public String getSubmitEndTime() {
		return submitEndTime;
	}

	public void setSubmitEndTime(String submitEndTime) {
		this.submitEndTime = submitEndTime;
	}

}
