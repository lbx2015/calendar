package net.riking.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.dao.ReportDao;
import net.riking.entity.model.Report;
import net.riking.entity.model.ReportFrequency;
import net.riking.service.ReportService;

@Service("reportService")
public class ReportServiceImpl implements ReportService{
	@Autowired
	ReportDao reportDao;
	

	@Override
	public List<ReportFrequency> findAppUserReportById(String userId) {
		// TODO Auto-generated method stub
		return reportDao.findAppUserReportById(userId);
	}

	@Override
	public List<Report> getAllReport() {
		// TODO Auto-generated method stub
		 List<Report> list =  reportDao.getAllReportByParams("");
		 return list;
	}
	
	

}
