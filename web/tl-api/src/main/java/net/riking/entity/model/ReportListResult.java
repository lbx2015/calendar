package net.riking.entity.model;

import java.util.List;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/***
 * 报表机构结构化结果集
 * @author james.you
 * @version crateTime：2017年12月21日 上午10:20:23
 * @used TODO
 */
public class ReportListResult extends BaseEntity {
	@Comment("机构编号")
	private String agenceCode;

	@Comment("机构中文名称")
	private String agenceName;
	
	@Comment("报表模块类别")
	public List<ReportMoudleTypeResult> list;

	public String getAgenceCode() {
		return agenceCode;
	}

	public void setAgenceCode(String agenceCode) {
		this.agenceCode = agenceCode;
	}

	public String getAgenceName() {
		if (agenceCode.toUpperCase().equals("CBRC")) {
			agenceName = "银监会";
		} else if (agenceCode.toUpperCase().equals("PBOC")) {
			agenceName = "中国人民银行";
		}
		return agenceName;
	}

	public List<ReportMoudleTypeResult> getList() {
		return list;
	}

	public void setList(List<ReportMoudleTypeResult> list) {
		this.list = list;
	}

	public void setAgenceName(String agenceName) {
		this.agenceName = agenceName;
	}

}
