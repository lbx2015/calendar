package net.riking.entity.model;

import java.util.List;

import net.riking.core.entity.BaseEntity;

public class ReportAgence extends BaseEntity {
	private String agenceName;

	private List<BaseModelPropdict> list;

	public String getAgenceName() {
		return agenceName;
	}

	public void setAgenceName(String agenceName) {
		this.agenceName = agenceName;
	}

	public List<BaseModelPropdict> getList() {
		return list;
	}

	public void setList(List<BaseModelPropdict> list) {
		this.list = list;
	}

}
