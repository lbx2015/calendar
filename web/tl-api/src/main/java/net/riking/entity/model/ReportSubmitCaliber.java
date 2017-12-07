package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import net.riking.core.annos.Comment;
import net.riking.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("报送口径配置")
@Entity
@Table(name = "t_report_submit_caliber")
public class ReportSubmitCaliber extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2800949423639540790L;

	@Id
	@Column(name = "Id", length = 32)
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@JsonProperty("reportSubmitCaliberId")
	private String id;

	@Comment("FK t_report_list")
	@Column(name = "report_id", length = 32)
	private String reportId;

	@Comment("口径类型（1-标准；2-分支机构；3-法人；4-合并；5-其它）")
	@Column(name = "caliber_type")
	private Integer caliberType;

	@Comment("频度（日、周、旬、月、季、半年、年）")
	@Column(name = "frequency")
	private Integer frequency;

	@Comment("延后天数")
	@org.hibernate.annotations.ColumnDefault("0")
	@Column(name = "delay_dates", length = 3)
	private Integer delayDates;

	@Comment("报送截止时间点0-23点")
	@Column(name = "submit_time", length = 2)
	private Integer submitTime;

	@Comment("是否工作日：0-自然日；1-工作日")
	@Column(name = "is_work_day", length = 1)
	private Integer isWorkDay;

	@Comment("是否启用：0-禁用；1-启用")
	@Column(name = "enabled", length = 1)
	private Integer enabled;

	@Comment("是否启用：0-删除；1-显示")
	@Column(name = "is_delete", length = 1)
	private String isDelete;

	// @Column(name = "remarks", length = 256)
	// private String remarks;

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

	public Integer getCaliberType() {
		return caliberType;
	}

	public void setCaliberType(Integer caliberType) {
		this.caliberType = caliberType;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

}
