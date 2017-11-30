package net.riking.entity.model;

import java.io.Serializable;

/**
 * 用户报表订阅复合主键表
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
public class RSRelUnionPkId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1119296926896750706L;

	public RSRelUnionPkId() {
	}

	public RSRelUnionPkId(String userId, String reportId) {
		super();
		this.userId = userId;
		this.reportId = reportId;
	}

	private String userId;

	private String reportId;

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
		RSRelUnionPkId other = (RSRelUnionPkId) obj;
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

}
