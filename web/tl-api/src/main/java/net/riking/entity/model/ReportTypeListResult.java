package net.riking.entity.model;

import java.util.List;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

public class ReportTypeListResult extends BaseEntity {
	@Comment("机构编号")
	private String agenceCode;

	@Comment("报表所属模块")
	private String moduleType;

	@Comment("报表所属模块名称")
	private String moduleTypeName;

	List<ReportResult> list;

	public String getAgenceCode() {
		return agenceCode;
	}

	public void setAgenceCode(String agenceCode) {
		this.agenceCode = agenceCode;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public List<ReportResult> getList() {
		return list;
	}

	public void setList(List<ReportResult> list) {
		this.list = list;
	}

	public String getModuleTypeName() {
		return moduleTypeName;
	}

	public void setModuleTypeName(String moduleTypeName) {
		this.moduleTypeName = moduleTypeName;
	}

}
