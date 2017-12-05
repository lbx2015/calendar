package net.riking.service;

import java.util.List;

import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;

public interface ReportService {
	
	/***
	 * 获取所有报表
	 * @author james.you
	 * @version crateTime：2017年12月4日 上午10:10:47
	 * @used TODO
	 * @return
	 */
	List<ReportListResult> getAllReport();
	
	/***
	 * 根据code和title，模糊查询报表集合
	 * @author james.you
	 * @version crateTime：2017年12月5日 上午10:45:13
	 * @used TODO
	 * @param param
	 * @return
	 */
	List<ReportResult> getReportByParam(String param);
	
	List<ReportFrequency> findAppUserReportById(String userId);

}
