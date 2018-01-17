package net.riking.entity.model;

import java.util.List;

import net.riking.core.entity.BaseEntity;

public class BaseModelPropdict extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6464919536350140511L;

	private String id;

	private String ke;

	private String valueName;

	public List<ReportFrequency> list;

	public BaseModelPropdict(String id, String ke, String valueName) {
		this.id = id;
		this.ke = ke;
		this.valueName = valueName;
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

	public String getValueName() {
		return valueName;
	}

	public void setValueName(String valueName) {
		this.valueName = valueName;
	}

	public List<ReportFrequency> getList() {
		return list;
	}

	public void setList(List<ReportFrequency> list) {
		this.list = list;
	}

}
