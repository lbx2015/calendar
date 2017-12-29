package net.riking.dao;

import java.util.List;

import net.riking.core.entity.PageQuery;
import net.riking.entity.resp.CurrentReportTaskResp;
import net.riking.entity.resp.ReportCompletedRelResult;

public interface ReportCompletedRelDao {
	
	/***
	 * 查询用户逾期任务
	 * @author james.you
	 * @version crateTime：2017年12月15日 下午6:26:59
	 * @used TODO
	 * @param userId
	 * @return
	 */
	List<ReportCompletedRelResult> findExpireReportByPage(String userId, PageQuery pageQuery);
	
	/***
	 * 查询用户历史核销任务
	 * @author james.you
	 * @version crateTime：2017年12月15日 下午6:26:59
	 * @used TODO
	 * @param userId
	 * @return
	 */
	List<ReportCompletedRelResult> findHisCompletedReportByPage(String userId, PageQuery pageQuery);
	
	/**
	 * 查询用户当天已完成/未完成的报表任务
	 * @author james.you
	 * @version crateTime：2017年12月16日 下午4:46:35
	 * @used TODO
	 * @param userId
	 * @param currentDate
	 * @return
	 */
	List<CurrentReportTaskResp> findCurrentTasks(String userId, String currentDate);
	
	/**
	 * 查询当天未完成报表任务的用户
	 * @author james.you
	 * @version crateTime：2017年12月16日 下午4:46:35
	 * @used TODO
	 * @param userId
	 * @param currentDate yyyyMMdd
	 * @return
	 */
	List<CurrentReportTaskResp> findUsersByCurrentDayTasks(String currentDate);
}
