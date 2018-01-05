package net.riking.entity.params;

import net.riking.core.entity.BaseEntity;

/**
 * 订阅报表参数
 * @author james.you
 * @version crateTime：2017年11月7日 下午4:01:44
 * @used TODO
 */
public class SubscribeReportParam extends BaseEntity{

	private String userId;

	// 订阅的reportIds
	private String reportIds;

	// //上报日期
	// private String submitDate;

	// 订阅类型 0-取消订阅 1-订阅
	private Integer subscribeType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getSubscribeType() {
		return subscribeType;
	}

	public void setSubscribeType(Integer subscribeType) {
		this.subscribeType = subscribeType;
	}

	public String getReportIds() {
		return reportIds;
	}

	public void setReportIds(String reportIds) {
		this.reportIds = reportIds;
	}

}
