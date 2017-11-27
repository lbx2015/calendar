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

	public RSRelUnionPkId(String user_id, String reportId) {
		super();
		this.user_id = user_id;
		this.reportId = reportId;
	}

	private String user_id;

	private String reportId;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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
		result = prime * result + ((user_id == null) ? 0 : user_id.hashCode());
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
		if (user_id == null) {
			if (other.user_id != null)
				return false;
		} else if (!user_id.equals(other.user_id))
			return false;
		return true;
	}

}
