package net.riking.service;

import java.util.List;

import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;

public interface ReportService {
	
	
	/***
	 * 根据code和title，模糊查询报表集合
	 * 不传责获取所有报表
	 * @author james.you
	 * @version crateTime：2017年12月5日 上午10:45:13
	 * @used TODO
	 * @param reportName
	 * @return
	 */
	List<ReportListResult> getReportByParam(String reportName);
	
	List<ReportResult> getReportResultByParam(String reportName);
	
	List<ReportFrequency> findAppUserReportById(String userId);

}
