package net.riking.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.dao.ReportDao;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;
import net.riking.entity.model.ReportTypeListResult;
import net.riking.service.ReportService;

@Service("reportService")
public class ReportServiceImpl implements ReportService {
	@Autowired
	ReportDao reportDao;

	@Override
	public List<ReportFrequency> findAppUserReportById(String userId) {
		// TODO Auto-generated method stub
		return reportDao.findAppUserReportById(userId);
	}

	@Override
	public List<ReportResult> getReportResultByParam(String reportName, String userId) {
		return reportDao.getAllReportByParams(reportName, userId);
	}

	@Override
	public List<ReportListResult> getReportByParam(String reportName, String userId) {
		// TODO Auto-generated method stub

		List<ReportResult> list = reportDao.getAllReportByParams(reportName, userId);

		List<ReportTypeListResult> typeListResults = new ArrayList<ReportTypeListResult>();

		List<ReportListResult> reportListResults = new ArrayList<ReportListResult>();

		for (int i = 0; i < list.size(); i++) {
			// 塞ReportTypeListResult第二层数据
			if (typeListResults.size() == 0) {
				setReportTypeListResult(list, i, typeListResults);
			}
			for (int j = 0; j < typeListResults.size(); j++) {
				// 把报表类型相等并且报表所属模块相等的归一类
				if (list.get(i).getReportType().toUpperCase().equals(typeListResults.get(j).getAgenceCode())
						&& list.get(i).getModuleType().equals(typeListResults.get(j).getModuleType())) {
					if (!typeListResults.get(j).getList().contains(list.get(i)))
						typeListResults.get(j).getList().add(list.get(i));
				} else {
					if (j == typeListResults.size() - 1) {
						setReportTypeListResult(list, i, typeListResults);
					}
				}
				// 塞ReportListResult第三层数据,在第二层数据塞完才执行
				if (list.size() - 1 == i) {
					if (reportListResults.size() == 0) {
						setReportListResult(typeListResults, j, reportListResults);
					}
					for (int k = 0; k < reportListResults.size(); k++) {
						if (reportListResults.get(k).getAgenceCode().toUpperCase()
								.equals(typeListResults.get(j).getAgenceCode())) {
							reportListResults.get(k).getList().add(typeListResults.get(j));
						} else {
							if (k == reportListResults.size() - 1) {
								setReportListResult(typeListResults, j, reportListResults);
							}
						}
					}
				}
			}
		}

		return reportListResults;
	}

	private void setReportListResult(List<ReportTypeListResult> typeListResults, int j,
			List<ReportListResult> reportListResults) {
		ReportListResult reportListResult = new ReportListResult();
		reportListResult.setAgenceCode(typeListResults.get(j).getAgenceCode());
		List<ReportTypeListResult> results = new ArrayList<ReportTypeListResult>();
		results.add(typeListResults.get(j));
		reportListResult.setList(results);
		reportListResults.add(reportListResult);
	}

	private void setReportTypeListResult(List<ReportResult> list, int i, List<ReportTypeListResult> typeListResults) {
		ReportTypeListResult typeListResult = new ReportTypeListResult();
		typeListResult.setAgenceCode(list.get(i).getReportType());
		typeListResult.setModuleType(list.get(i).getModuleType());
		typeListResult.setModuleTypeName(list.get(i).getModuleTypeName());
		List<ReportResult> results = new ArrayList<ReportResult>();
		results.add(list.get(i));
		typeListResult.setList(results);
		typeListResults.add(typeListResult);
	}

}
