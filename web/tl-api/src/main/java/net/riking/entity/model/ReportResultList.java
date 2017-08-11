package net.riking.entity.model;

import java.util.ArrayList;
import java.util.List;

public class ReportResultList {
	private List<ReportResult> list;

	public List<ReportResult> getList() {
		if(list==null){
			list = new ArrayList<ReportResult>();
		}
		return list;
	}

	public void setList(List<ReportResult> list) {
		this.list = list;
	}
	
}
