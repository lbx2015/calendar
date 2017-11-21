package net.riking.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.dao.ReportListDao;
import net.riking.entity.model.ReportFrequency;
import net.riking.service.ReportLisService;

@Service("reportListService")
public class ReportListServiceImpl implements ReportLisService{
	@Autowired
	ReportListDao reportListDao;

	@Override
	public List<ReportFrequency> findAppUserReportById(String userId) {
		// TODO Auto-generated method stub
		return reportListDao.findAppUserReportById(userId);
	}

}
