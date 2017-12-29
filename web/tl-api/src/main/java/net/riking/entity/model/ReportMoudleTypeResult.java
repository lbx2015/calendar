package net.riking.entity.model;

import java.util.List;

import net.riking.core.annos.Comment;
import net.riking.core.entity.BaseEntity;

/***
 * 报表模块类别结果集
 * @author james.you
 * @version crateTime：2017年12月21日 上午10:20:02
 * @used TODO
 */
public class ReportMoudleTypeResult extends BaseEntity {
	@Comment("报表所属模块")
	private String moduleType;

	@Comment("报表所属模块名称")
	private String moduleTypeName;
	
	List<ReportResult> list;

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
