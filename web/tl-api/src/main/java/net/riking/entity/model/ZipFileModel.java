package net.riking.entity.model;

import java.util.Date;

public class ZipFileModel {
	private String name;
	private String type;
	private Date creatTime;
	private String submitBatch;

	public String getName() {
		return name;
	}

	public ZipFileModel(String name, String type, Date creatTime, String submitBatch) {
		super();
		this.name = name;
		this.type = type;
		this.creatTime = creatTime;
		this.submitBatch = submitBatch;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getSubmitBatch() {
		return submitBatch;
	}

	public void setSubmitBatch(String submitBatch) {
		this.submitBatch = submitBatch;
	}

	public ZipFileModel() {
		super();
	}
}
