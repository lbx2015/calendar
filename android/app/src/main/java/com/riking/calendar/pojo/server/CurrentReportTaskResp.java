package com.riking.calendar.pojo.server;

/**
 * 工作台当前报表核销任务
 * @author james.you
 * @version crateTime：2017年12月16日 下午5:37:01
 * @used TODO
 */
public class CurrentReportTaskResp  {
	public String reportId;
	public String reportCode;
	public String reportName;//报表名称
	public int frequency;//报表频度
	public String reportBatch;//批次0,1,2,3,4, 其它
	public String submitStartTime;//上报开始时间yyyyMMdd
	public String submitEndTime;//上报结束时间yyyyMMdd
	public String isCompleted;//是否完成：0-未完成；1-完成
	public String remindId;//提醒记录Id
	public String remindContent;//提醒的报表内容
	
}
