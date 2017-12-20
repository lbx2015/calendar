package net.riking.dao;

import java.awt.print.Pageable;
import java.util.List;

import net.riking.core.entity.PageQuery;
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
	
}
