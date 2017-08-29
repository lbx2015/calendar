package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_days")
public class Days {

	// 日期（yyyyMMdd）
	@Id
	@Column(name = "dates", length = 8)
	private String date;
	// 星期(1-7)代表星期一到星期天
	@Column(name = "weekday", length = 1)
	private String weekday;
	// 工作日标识 1：工作日 0：非工作日
	@Column(name = "is_work")
	private Integer isWork;

	public Days() {

	}

	public Days(Integer isWork, String date) {
		this.isWork = isWork;
		this.date = date;
	}

	public String getWeekday() {
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getIsWork() {
		return isWork;
	}

	public void setIsWork(Integer isWork) {
		this.isWork = isWork;
	}

}
