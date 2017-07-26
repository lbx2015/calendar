package net.riking.entity.model;

import java.util.Date;

public class dataReportQuery {
	//开始时间
	private Date star ;
	//结束时间
	private Date end;
	//模块
	private String modular;
	//状态
	private String state;
	public Date getStar() {
		return star;
	}
	public void setStar(Date star) {
		this.star = star;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getModular() {
		return modular;
	}
	public void setModular(String modular) {
		this.modular = modular;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	

}
