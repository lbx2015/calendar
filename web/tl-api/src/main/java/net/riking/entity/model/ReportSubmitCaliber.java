package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import net.riking.entity.BaseEntity;

@Entity
@Table(name = "t_report_submit_caliber")
public class ReportSubmitCaliber extends BaseEntity {

	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	private String id;

	// FK t_report_list
	@Column(name = "report_id", length = 32)
	private String reportId;

	// 口径类型（1-标准；2-分支机构；3-法人；4-合并；5-其它）
	@Column(name = "caliber_type", length = 2)
	private String caliberType;

	// 频度（日、周、旬、月、季、半年、年）
	@Column(name = "frequency", length = 1)
	private String frequency;

	// 延后天数
	@Column(name = "delay_dates", length = 3)
	private Integer delayDates;

	// 报送截止时间点0-23点
	@Column(name = "submit_time", length = 2)
	private Integer submitTime;

	// 是否工作日：0-自然日；1-工作日
	@Column(name = "is_work_day", length = 1)
	private Integer isWorkDay;

	// 是否启用：0-禁用；1-启用
	@Column(name = "enabled", length = 1)
	private Integer enabled;

	// 是否启用：0-禁用；1-启用
	@Column(name = "delete_state", length = 1)
	private Integer deleteState;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getCaliberType() {
		return caliberType;
	}

	public void setCaliberType(String caliberType) {
		this.caliberType = caliberType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Integer getDelayDates() {
		return delayDates;
	}

	public void setDelayDates(Integer delayDates) {
		this.delayDates = delayDates;
	}

	public Integer getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Integer submitTime) {
		this.submitTime = submitTime;
	}

	public Integer getIsWorkDay() {
		return isWorkDay;
	}

	public void setIsWorkDay(Integer isWorkDay) {
		this.isWorkDay = isWorkDay;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Integer getDeleteState() {
		return deleteState;
	}

	public void setDeleteState(Integer deleteState) {
		this.deleteState = deleteState;
	}


}
