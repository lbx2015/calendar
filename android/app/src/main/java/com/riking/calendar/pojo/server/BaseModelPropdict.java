package com.riking.calendar.pojo.server;

import java.util.List;

public class BaseModelPropdict {

	public String id;

	public String ke;

	public String value;
	
	public List<ReportFrequency> list;
	
	public BaseModelPropdict(String id,String ke,String value){
		this.id = id;
		this.ke = ke;
		this.value = value;
	}
}
