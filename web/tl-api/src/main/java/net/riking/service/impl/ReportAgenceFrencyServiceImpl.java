package net.riking.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.dao.ReportAgenceFrencyDao;
import net.riking.entity.model.BaseModelPropdict;
import net.riking.entity.model.ReportFrequency;
import net.riking.service.ReportAgenceFrencyService;

@Service("reportAgenceFrencyService")
public class ReportAgenceFrencyServiceImpl implements ReportAgenceFrencyService {
	
	@Autowired
	ReportAgenceFrencyDao reportAgenceFrencyDao;

	@Override
	public Set<String> findALLAgence() {
		// TODO Auto-generated method stub
		return reportAgenceFrencyDao.findALLAgence();
	}

	@Override
	public List<BaseModelPropdict> findAgenceNameList( String value) {
		// TODO Auto-generated method stub
		return reportAgenceFrencyDao.findAgenceNameList(value);
	}

	@Override
	public List<ReportFrequency> findReportByModuleType(String moduleType) {
		// TODO Auto-generated method stub
		return reportAgenceFrencyDao.findReportByModuleType(moduleType);
	}

	@Override
	public List<ReportFrequency> findReportListByName(String reportName) {
		// TODO Auto-generated method stub
		return reportAgenceFrencyDao.findReportListByName(reportName);
	}

}
