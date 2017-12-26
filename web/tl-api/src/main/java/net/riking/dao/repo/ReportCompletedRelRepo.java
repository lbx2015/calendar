package net.riking.dao.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ReportCompletedRel;

@Repository
public interface ReportCompletedRelRepo extends JpaRepository<ReportCompletedRel, String>,
		JpaSpecificationExecutor<ReportCompletedRel>, PagingAndSortingRepository<ReportCompletedRel, String> {

	@Query("select r.reportId from ReportCompletedRel r where r.userId = ?1 and r.completedDate = ?2")
	List<String> findNowReport(String userId, String completedDate);
	
	/***
	 * 获取用户当月之后所有任务
	 * @author james.you
	 * @version crateTime：2017年12月20日 下午3:50:31
	 * @used TODO
	 * @param userId
	 * @param currentMonth 当月yyyyMM
	 * @return
	 */
	@Query("select new ReportCompletedRel(userId, reportId, completedDate, isCompleted, SUBSTRING(submitStartTime, 1, 8), SUBSTRING(submitEndTime, 1, 8) ) from ReportCompletedRel where userId = ?1 and ?2 >= SUBSTRING(submitStartTime, 1, 6) and ?2 <= SUBSTRING(submitEndTime, 1, 6) ")
	List<ReportCompletedRel> findTasksByUserId(String userId, String currentMonth);
	
	@Query(" from ReportCompletedRel where userId = ?1 and reportId = ?2 and submitStartTime = ?3 and submitEndTime = ?4 ")
	ReportCompletedRel findByOne(String userId, String reportId, String submitStartTime, String submitEndTime);

	/***
	 * 核销报表修改为未完成
	 * @author james.you
	 * @version crateTime：2017年12月16日 下午6:52:02
	 * @used TODO
	 * @param userId
	 * @param reportId
	 * @param submitStartTime
	 * @param submitEndTime
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("update ReportCompletedRel set isCompleted=0, completed_date='' where userId = ?1 and reportId = ?2 and submitStartTime = ?3 and submitEndTime = ?4 ")
	public int updateNotComplated(String userId, String reportId, String submitStartTime, String submitEndTime);
	
	/***
	 * 核销报表修改为已完成
	 * @author james.you
	 * @version crateTime：2017年12月16日 下午6:52:38
	 * @used TODO
	 * @param userId
	 * @param reportId
	 * @param submitStartTime
	 * @param submitEndTime
	 * @param completedDate
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("update ReportCompletedRel set isCompleted=1, completedDate=?5 where userId = ?1 and reportId = ?2 and submitStartTime = ?3 and submitEndTime = ?4 ")
	public int updateComplated(String userId, String reportId, String submitStartTime, String submitEndTime, String completedDate);

	/***
	 * 删除在该次订阅的时间范围内用户核销相关数据
	 * @author james.you
	 * @version crateTime：2017年12月18日 下午1:34:52
	 * @used TODO
	 * @param userId
	 * @param reportIds
	 * @param submitDate yyyyMMdd
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("delete from ReportCompletedRel where userId= ?1 and reportId not in (?2) and ?3 between submitStartTime and submitEndTime ")
	public int deleteSubscriptTask(String userId, String[] reportIds, String submitDate);

	
}
