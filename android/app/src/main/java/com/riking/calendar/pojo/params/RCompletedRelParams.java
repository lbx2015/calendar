package com.riking.calendar.pojo.params;

import java.util.Date;

/**
 * 核销信息的接收参数
 *
 * @author jc.tan 2017年11月28日
 * @see
 * @since 1.0
 */
public class RCompletedRelParams extends BaseParams {
    // 报表id
    public String reportId;
    // 提醒时间（yyyyMMdd）
    public Date remindTime;
    //是否完成：0-未完成；1-完成
    public int isCompleted;
    //上报开始时间yyyyMMddHHmm
    public String submitStartTime;
    //上报结束时间yyyyMMddHHmm
    public String submitEndTime;
    //报表完成时间yyyyMMddHHmm
    public String completedDate;
    //闹钟提醒Id
    public String remindId;
    //当前选择的日期yyyyMMdd
    public String currentDate;

}
