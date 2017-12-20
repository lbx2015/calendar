package net.riking.entity.params;

/**
 * 订阅报表参数
 * @author james.you
 * @version crateTime：2017年11月7日 下午4:01:44
 * @used TODO
 */
public class SubscribeReportParam {

	private String userId;

	// 订阅的reportIds
	private String reportIds;
	// //上报日期
	// private String submitDate;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getReportIds() {
		return reportIds;
	}

	public void setReportIds(String reportIds) {
		this.reportIds = reportIds;
	}

}
