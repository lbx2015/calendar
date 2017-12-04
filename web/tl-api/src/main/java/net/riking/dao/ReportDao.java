package net.riking.dao;

import java.util.List;

import net.riking.entity.model.Report;
import net.riking.entity.model.ReportFrequency;

public interface ReportDao {
	List<ReportFrequency> findAppUserReportById(String userId);

	/***
	 * 根据code和title参数值，筛选获取报表
	 * @author james.you
	 * @version crateTime：2017年12月4日 上午10:17:16
	 * @used TODO
	 * @param code和title参数值
	 * @return
	 */
	List<Report> getAllReportByParams(String param);
}
