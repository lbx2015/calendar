package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import net.riking.core.annos.Comment;

/**
 * 
 * @author jc.tan 2017年11月27日
 * @see
 * @since 1.0
 */
@Comment("系统日历表")
@Entity
@Table(name = "t_sys_days")
public class SysDays {

	@Comment("日期（yyyyMMdd）")
	@Id
	@Column(name = "dates", length = 8)
	private String dates;

	@Comment("星期(1-7)代表星期一到星期天")
	@Column(name = "weekday", length = 1)
	private Integer weekday;

	@Comment("工作日标识 1：工作日 0：非工作日")
	@Column(name = "iswork")
	private Integer isWork;

	public SysDays() {

	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
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

	
}
