package com.riking.calendar.pojo;

/**
 * 记录用户每天报表完成的状态
 *
 * @author lucky.liu
 * @version crateTime：2017年8月10日 下午7:01:46
 * @used TODO
 */
//@Entity
//@Table(name = "t_app_user_report_complete_rel")
public class AppUserReportCompleteRel {

    //	@Id
//	@Column(name = "complete_id", length = 17)
    public String completeId;

    // 用户ID"
//	@Column(name = "app_user_id", length = 32)
    public String appUserId;

    // 报表ID"
//	@Column(name = "report_id", length = 32)
    public String reportId;

    // 报表完成时间（yyyyMMdd）
//	@Column(name = "complete_date", length = 8)
    public String completeDate;

    // 同步标识app端数据状态
//	@Transient
    public Integer isComplete;
}
