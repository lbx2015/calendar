package net.riking.entity.model;

import java.util.List;

public class BaseModelPropdict {

	private String id;
	
	private String ke;
	
	private String value;
	
	public List<ReportFrequency> list;
	
	public BaseModelPropdict(String id,String ke,String value){
		this.id = id;
		this.ke = ke;
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKe() {
		return ke;
	}

	public void setKe(String ke) {
		this.ke = ke;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<ReportFrequency> getList() {
		return list;
	}

	public void setList(List<ReportFrequency> list) {
		this.list = list;
	}

}
