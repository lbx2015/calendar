package net.riking.service;

import java.util.List;

import net.riking.entity.model.Report;
import net.riking.entity.model.ReportFrequency;

public interface ReportService {
	
	/***
	 * 获取所有报表
	 * @author james.you
	 * @version crateTime：2017年12月4日 上午10:10:47
	 * @used TODO
	 * @return
	 */
	List<Report> getAllReport();
	
	List<ReportFrequency> findAppUserReportById(String userId);

}
