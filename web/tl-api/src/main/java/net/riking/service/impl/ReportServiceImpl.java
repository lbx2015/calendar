package net.riking.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.dao.ReportDao;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;
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
	public List<ReportListResult> getReportByParam(String reportName) {
		// TODO Auto-generated method stub

		List<ReportResult> list = reportDao.getAllReportByParams(reportName);
		
		Map<String, List<ReportResult>> m = new HashMap<String, List<ReportResult>>();
		List<ReportResult> rList = null;
		for (ReportResult r : list) {
			rList = m.get(r.getReportType().toUpperCase());
			if (rList == null) {
				rList = new ArrayList<ReportResult>();
			}
			rList.add(r);
			m.put(r.getReportType().toUpperCase(), rList);
		}
		

		List<ReportListResult> result = new ArrayList<ReportListResult>();
		Iterator<String> it = m.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			List<ReportResult> li = m.get(key);
			ReportListResult reportListResult = new ReportListResult();
			reportListResult.setAgenceCode(key);
			reportListResult.setList(li);
			result.add(reportListResult);
		}
		
		return result;
	}

}
