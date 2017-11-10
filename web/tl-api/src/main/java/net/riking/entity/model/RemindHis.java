package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.riking.entity.PageQuery;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 下午4:35:23
 * @used TODO
 */
@Entity
@Table(name = "t_remind_his")
public class RemindHis extends PageQuery {

	// 手机端时间戳：yyyyMMddHHmmssSSS
	@Id
	@Column(name = "remind_his_id", length = 17)
	private String remindHisId;

	// 用户Id
	@Column(name = "user_id", length = 32)
	private String userId;

	// 提醒内容
	@Column(name = "content", length = 255)
	private String content;

	// 提醒时间
	@Column(name = "start_time", length = 4)
	private String startTime;

	// 提醒日期
	@Column(name = "str_date", length = 14)
	private String strDate;

	// 同步标识app端数据状态
	@Transient
	private int deleteState;
	// 提醒日期
	@Column(name = "report_Id", length = 17)
	private String reportId;

	// 客户端数据来源：1-IOS;2-Android;3-其它
	@Column(name = "client_type", length = 1)
	private String clientType;

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	public int getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(int deleteState) {
		this.deleteState = deleteState;
	}

	public String getRemindHisId() {
		return remindHisId;
	}

	public void setRemindHisId(String remindHisId) {
		this.remindHisId = remindHisId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}
	
	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	@Override
	public String toString() {
		return "RemindHis [remindHisId=" + remindHisId + ", userId=" + userId + ", content=" + content + ", startTime="
				+ startTime + ", strDate=" + strDate + ", deleteState=" + deleteState + ", reportId=" + reportId
				+ ", clientType=" + clientType + "]";
	}

}
