package net.riking.entity.model;

import java.util.List;

import net.riking.core.annos.Comment;

public class ReportListResult {
	@Comment("机构编号")
	private String agenceCode;
	
	@Comment("机构中文名称")
	private String agenceName;
	
	public List<ReportResult> list;

	public String getAgenceCode() {
		return agenceCode;
	}

	public void setAgenceCode(String agenceCode) {
		this.agenceCode = agenceCode;
	}

	public String getAgenceName() {
		if(agenceCode.toUpperCase().equals("CBRC")){
			agenceName = "银监会";
		}else if(agenceCode.toUpperCase().equals("PBOC")){
			agenceName = "中国人民银行";
		}
		return agenceName;
	}


	public List<ReportResult> getList() {
		return list;
	}

	public void setList(List<ReportResult> list) {
		this.list = list;
	}
	
}
