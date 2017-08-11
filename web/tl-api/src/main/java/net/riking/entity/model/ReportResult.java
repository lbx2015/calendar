package net.riking.entity.model;

import java.util.List;

public class ReportResult {

	private String title;
	private List<QueryReport> result;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<QueryReport> getResult() {
		return result;
	}
	public void setResult(List<QueryReport> result) {
		this.result = result;
	}

	

}
