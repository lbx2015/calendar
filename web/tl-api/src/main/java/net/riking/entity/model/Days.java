package net.riking.entity.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_days")
public class Days {
	
	@Id
	@Column(name = "dates", length = 17)
	private String date;
	@Column(name = "weekday", length = 1)
	private String weekday;
	@Column(name = "remark", length = 1)
	private String remark;

	public Days(){
		
	}
	
	public Days(String remark, String date) {
		this.remark = remark;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
