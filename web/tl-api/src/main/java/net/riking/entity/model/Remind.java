package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.riking.core.annos.Comment;
import net.riking.entity.PageQuery;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("提醒表")
@Entity
@Table(name = "t_remind")
public class Remind extends PageQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4060546025883551207L;

	/**
	 * @author Lucky.Liu on 2017/8/05.
	 */
	@Comment("手机端时间戳：yyyyMMddHHmmssSSS")
	@Id
	@Column(name = "remind_id", length = 17)
	private String remindId;

	@Comment("用户Id: pk t_app_user")
	@Column(name = "user_id", length = 32)
	private String userId;

	@Comment("报表id")
	@Column(name = "report_id", length = 32)
	private String reportId;

	@Comment("内容")
	@Column(name = "content", length = 255)
	private String content;

	// 提醒时间
	@Comment("提醒时间：HHmm")
	@Column(name = "start_time", length = 5)
	private String startTime;

	// 提醒日期
	@Comment("提醒时间：yyyyMMdd ")
	@Column(name = "str_date", length = 14)
	private String strDate;

	@Comment("是否全天提醒(0-否；1-是)")
	@Column(name = "is_allday", length = 1)
	private Integer isAllday;

	@Comment("提前提醒时间（不选默认为0）")
	@Column(name = "before_time", length = 2)
	private Integer beforeTime;

	@Comment("结束时间：HHmm")
	@Column(name = "end_time", length = 4)
	private String endTime;

	@Comment("重复标识（0-不重复；1-法定工作日；2-法定节假日；3-其它）")
	@Column(name = "repeat_flag", length = 8)
	private int repeatFlag;

	@Comment("重复值(例如0,1,2,3,4,5,6)")
	@Column(name = "repeat_value", length = 20)
	private String repeatValue;

	@Comment("当前星期数（0,6）")
	@Column(name = "curr_week", length = 1)
	private Integer currWeek;

	@Comment("报送开始时间（yyyyMMddHHmm）")
	@Column(name = "submit_start_time", length = 8)
	private String submitStartTime;

	@Comment("报送截止时间（yyyyMMddHHmm）")
	@Column(name = "submit_end_time", length = 8)
	private String submitEndTime;

	// 同步标识app端数据状态
	@Transient
	private int deleteState;

	@Comment("客户端数据来源：1-IOS;2-Android;3-其它")
	@Column(name = "client_type", length = 1)
	private Integer clientType;

	public String getRemindId() {
		return remindId;
	}

	public void setRemindId(String remindId) {
		this.remindId = remindId;
	}

	public String getSubmitStartTime() {
		return submitStartTime;
	}

	public void setSubmitStartTime(String submitStartTime) {
		this.submitStartTime = submitStartTime;
	}

	public String getSubmitEndTime() {
		return submitEndTime;
	}

	public void setSubmitEndTime(String submitEndTime) {
		this.submitEndTime = submitEndTime;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
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

	public Integer getIsAllday() {
		return isAllday;
	}

	public void setIsAllday(Integer isAllday) {
		this.isAllday = isAllday;
	}

	public Integer getBeforeTime() {
		return beforeTime;
	}

	public void setBeforeTime(Integer beforeTime) {
		this.beforeTime = beforeTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getRepeatFlag() {
		return repeatFlag;
	}

	public void setRepeatFlag(int repeatFlag) {
		this.repeatFlag = repeatFlag;
	}

	public String getRepeatValue() {
		return repeatValue;
	}

	public void setRepeatValue(String repeatValue) {
		this.repeatValue = repeatValue;
	}

	public Integer getCurrWeek() {
		return currWeek;
	}

	public void setCurrWeek(Integer currWeek) {
		this.currWeek = currWeek;
	}

	public int getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(int deleteState) {
		this.deleteState = deleteState;
	}

	public Integer getClientType() {
		return clientType;
	}

	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}

}
