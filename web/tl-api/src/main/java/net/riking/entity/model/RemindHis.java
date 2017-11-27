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
@Comment("提醒历史记录表")
@Entity
@Table(name = "t_remind_his")
public class RemindHis extends PageQuery {

	@Comment("手机端时间戳：yyyyMMddHHmmssSSS")
	@Id
	@Column(name = "remind_his_id", length = 17)
	private String remindHisId;

	@Comment("用户Id")
	@Column(name = "user_id", length = 32)
	private String userId;

	@Comment("提醒内容")
	@Column(name = "content", length = 255)
	private String content;

	@Comment("提醒时间:HHmm")
	@Column(name = "start_time", length = 4)
	private String startTime;

	@Comment("日期：yyyyMMdd")
	@Column(name = "str_date", length = 14)
	private String strDate;

	// 同步标识app端数据状态
	@Transient
	private int deleteState;

	// // 提醒日期
	// @Column(name = "report_Id", length = 17)
	// private String reportId;

	@Comment("客户端数据来源：1-IOS;2-Android;3-其它")
	@Column(name = "client_type", length = 1)
	private Integer clientType;

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
