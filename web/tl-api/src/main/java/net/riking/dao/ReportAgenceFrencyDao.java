package net.riking.dao;

import java.util.List;
import java.util.Set;

import net.riking.entity.model.BaseModelPropdict;
import net.riking.entity.model.ReportFrequency;

public interface ReportAgenceFrencyDao {

	//查询所有的汇报机构名称
	Set<String> findALLAgence();
	
	List<BaseModelPropdict> findAgenceNameList(String value);
	
	List<ReportFrequency> findReportByModuleType(String moduleType);
	
	List<ReportFrequency> findReportListByName(String reportName);
}
