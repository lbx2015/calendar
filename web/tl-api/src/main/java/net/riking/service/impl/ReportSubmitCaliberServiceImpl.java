package net.riking.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.dao.impl.ReportSubmitCaliberDaoImpl;
import net.riking.entity.model.QueryReport;
import net.riking.service.ReportSubmitCaliberService;
@Service("reportSubmitCaliberService")
public class ReportSubmitCaliberServiceImpl implements ReportSubmitCaliberService {
	@Autowired
	ReportSubmitCaliberDaoImpl reportSubmitCaliberDao;
	@Override
	public Set<QueryReport> findAllByFreeDatefromReportId(String userId, Integer week, Integer ten, Integer month,
			Integer season, Integer halfYear, Integer Year, Integer isWorkDay) {
		Set<QueryReport> list = reportSubmitCaliberDao.findAllByFreeDatefromReportId(userId, week, ten, month, season, halfYear, Year, isWorkDay);
		return list;
	}
	@Override
	public Set<QueryReport> findAllfromReportId() {
		Set<QueryReport> list = reportSubmitCaliberDao.findAllfromReportId();
		return list;
	}



}
