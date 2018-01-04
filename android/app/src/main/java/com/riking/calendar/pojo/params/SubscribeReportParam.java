package com.riking.calendar.pojo.params;

import java.util.List;

/**
 * 订阅报表参数
 * @author james.you
 * @version crateTime：2017年11月7日 下午4:01:44
 * @used TODO
 */
public class SubscribeReportParam extends BaseParams {
	//reportIds which is separated by the comma symbol. wtf.
	public String reportIds;

	// 订阅类型 0-取消订阅 1-订阅
	public int subscribeType;
}
