package net.riking.entity.model;

import java.util.Date;

public class Keyistatic {
	private String csum;
	private Integer kayivals;
	private Integer zongjiaoyiVals;
	private Date date;
	public String getCsum() {
		return csum;
	}
	public void setCsum(String csum) {
		this.csum = csum;
	}
	public Integer getKayivals() {
		return kayivals;
	}
	public void setKayivals(Integer kayivals) {
		this.kayivals = kayivals;
	}
	public Integer getZongjiaoyiVals() {
		return zongjiaoyiVals;
	}
	public void setZongjiaoyiVals(Integer zongjiaoyiVals) {
		this.zongjiaoyiVals = zongjiaoyiVals;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Keyistatic [csum=" + csum + ", kayivals=" + kayivals + ", zongjiaoyiVals=" + zongjiaoyiVals + ", date="
				+ date + "]";
	}
	
	
}
