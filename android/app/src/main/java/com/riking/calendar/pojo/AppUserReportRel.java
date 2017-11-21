package com.riking.calendar.pojo;

/**
 * Created by Lucky.Liu on 2017/8/05.
 */

//@Comment("用户报表")
//@Entity
//@Table(name = "t_appuser_report_rel")
public class AppUserReportRel {


    //	@Id
//	@Column(name = "Id", length = 32)
//	@GenericGenerator(name = "system-uuid", strategy = "uuid")
//	@GeneratedValue(generator = "system-uuid")
//    public String id;
    // 用户ID
//	@Column(name = "appUser_id", length = 32)
    public String appUserId;
    // 报表ID
//	@Column(name = "report_id", length = 32)
    public String reportId;
    // 是否完成
//	@Column(name = "is_complete", length = 32)
    public String isComplete;//0：未完成；1：已完成
    public String reportName;//
    public AppUserReportRel(String reportId) {
        super();
        this.reportId = reportId;
    }

    public AppUserReportRel() {
        // TODO Auto-generated constructor stub
    }

}
