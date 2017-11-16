package net.riking.service;

import java.util.List;
import java.util.Set;
import net.riking.entity.model.BaseModelPropdict;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportList;

public interface ReportAgenceFrencyService {
	//查询所有的汇报机构名称
	Set<String> findALLAgence();
	
	List<BaseModelPropdict> findAgenceNameList(String value);
	
	List<ReportFrequency> findReportByModuleType(String moduleType);
	
	List<ReportFrequency> findReportListByName(String reportName);
}
