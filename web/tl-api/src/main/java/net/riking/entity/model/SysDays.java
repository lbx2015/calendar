package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("系统日历表")
@Entity
@Table(name = "t_sys_days")
public class SysDays extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2163679539685532528L;

	@Comment("日期（yyyyMMdd）")
	@Id
	@Column(name = "dates", length = 8)
	private String dates;

	@Comment("星期(1-7)代表星期一到星期天")
	@Column(name = "weekday", length = 1)
	private Integer weekday;

	@Comment("工作日标识 1：工作日 0：非工作日")
	@Column(name = "is_work")
	private Integer isWork;

	@Comment("是否国家节假日：0-否；1-是")
	@Column(name = "is_holiday")
	private Integer isHoliday;

	@Comment("国家节假日是否启用： 0-禁用 1-启用")
	@org.hibernate.annotations.ColumnDefault("1")
	@Column(name = "enabled", nullable = false, precision = 1)
	private Integer enabled;

	@Comment("节日名称备注")
	@Column(name = "remark")
	private String remark;

	public SysDays() {

	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Integer getWeekday() {
		return weekday;
	}

	public void setWeekday(Integer weekday) {
		this.weekday = weekday;
	}

	public Integer getIsWork() {
		return isWork;
	}

	public void setIsWork(Integer isWork) {
		this.isWork = isWork;
	}

	public Integer getIsHoliday() {
		return isHoliday;
	}

	public void setIsHoliday(Integer isHoliday) {
		this.isHoliday = isHoliday;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
