package net.riking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.riking.entity.VO.ReportVO;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;

public interface ReportService {

	/***
	 * 根据code和title，模糊查询报表集合 不传责获取所有报表
	 * @author james.you
	 * @version crateTime：2017年12月5日 上午10:45:13
	 * @used TODO
	 * @param reportName
	 * @return
	 */
	List<ReportListResult> getReportByParam(String reportName, String userId);

	List<ReportResult> getReportResultByParam(String reportName, String userId);

	List<ReportFrequency> findAppUserReportById(String userId);
	
	
	/************ web **********/
	void saveOrUpdate(ReportVO reportVO);

	Page<ReportVO> findAll(ReportVO reportVO, PageRequest pageable);

}
