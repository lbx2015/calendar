package net.riking.entity.model;

public class Period {

	private Integer week;
	private Integer ten;
	private Integer month;
	private Integer season;
	private Integer halfYear;
	private Integer Year;

	public Period(Integer week, Integer ten, Integer month, Integer season, Integer halfYear, Integer year) {
		super();
		this.week = week;
		this.ten = ten;
		this.month = month;
		this.season = season;
		this.halfYear = halfYear;
		Year = year;
	}

	public Period() {
		super();
	}

	public Integer getWeek() {
		return week;
	}

	public void setWeek(Integer week) {
		this.week = week;
	}

	public Integer getTen() {
		return ten;
	}

	public void setTen(Integer ten) {
		this.ten = ten;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getSeason() {
		return season;
	}

	public void setSeason(Integer season) {
		this.season = season;
	}

	public Integer getHalfYear() {
		return halfYear;
	}

	public void setHalfYear(Integer halfYear) {
		this.halfYear = halfYear;
	}

	public Integer getYear() {
		return Year;
	}

	public void setYear(Integer year) {
		Year = year;
	}

	@Override
	public String toString() {
		return "Period [week=" + week + ", ten=" + ten + ", month=" + month + ", season=" + season + ", halfYear="
				+ halfYear + ", Year=" + Year + "]";
	}

}
