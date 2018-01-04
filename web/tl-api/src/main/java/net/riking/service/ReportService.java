package net.riking.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import net.riking.core.entity.PageQuery;
import net.riking.entity.VO.ReportVO;
import net.riking.entity.model.ReportFrequency;
import net.riking.entity.model.ReportListResult;
import net.riking.entity.model.ReportResult;
import net.riking.entity.resp.CurrentReportTaskResp;
import net.riking.entity.resp.ReportCompletedRelResult;

public interface ReportService {

	/***
	 * 根据code和title，模糊查询报表集合 不传责获取所有报表
	 * 
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

	/***
	 * 查询用户逾期任务
	 * 
	 * @author james.you
	 * @version crateTime：2017年12月15日 下午6:26:59
	 * @used TODO
	 * @param userId
	 * @return
	 */
	List<ReportCompletedRelResult> findExpireReportByPage(String userId, PageQuery pageQuery);

	/***
	 * 查询用户历史核销任务
	 * 
	 * @author james.you
	 * @version crateTime：2017年12月15日 下午6:26:59
	 * @used TODO
	 * @param userId
	 * @return
	 */
	List<ReportCompletedRelResult> findHisCompletedReportByPage(String userId, PageQuery pageQuery);

	/**
	 * 查询用户当天已完成/未完成的报表任务
	 * 
	 * @author james.you
	 * @version crateTime：2017年12月16日 下午4:46:35
	 * @used TODO
	 * @param userId
	 * @param currentDate
	 * @return
	 */
	List<CurrentReportTaskResp> findCurrentTasks(String userId, String currentDate);

	/***
	 * 根据用户新增订阅后，添加半年任务
	 * 
	 * @author james.you
	 * @version crateTime：2017年12月18日 下午8:08:18
	 * @used TODO
	 * @param userId
	 * @param reportIds
	 * @param currentDate
	 * @return
	 */
	boolean addReportTaskByUserSubscribe(String userId, String[] reportIds, String currentDate);

	/**
	 * 单个订阅报表
	 * @param userId
	 * @param reportId
	 * @param currentDate
	 * @param subscribeType
	 */
	boolean addReportTaskByUserSingleSubscribe(String userId, String[] reportIds, String currentDate,
			Integer subscribeType);

}
