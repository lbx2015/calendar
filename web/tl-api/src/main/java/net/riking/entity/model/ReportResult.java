package net.riking.entity.model;

import java.util.Set;

public class ReportResult {

	private String title;
	private Set<QueryReport> result;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Set<QueryReport> getResult() {
		return result;
	}
	public void setResult(Set<QueryReport> result) {
		this.result = result;
	}

	

}
