package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
	 * 查询逾期任务
	 * @author james.you
	 * @version crateTime：2017年12月15日 下午6:26:59
	 * @used TODO
	 * @param userId
	 * @param currentDate yyyyMMdd
	 * @return
	 */
	@Query("from ReportCompletedRel t where t.user_id = ?1 and t.is_completed = 0 and SUBSTR(t.submit_end_time, 1, 8) < ?2")
	List<ReportCompletedRel> findExpireReport(String userId, String currentDate);

	/***
	 * 查询历史核销任务
	 * @author james.you
	 * @version crateTime：2017年12月15日 下午6:26:59
	 * @used TODO
	 * @param userId
	 * @param currentDate yyyyMMdd
	 * @return
	 */
	@Query("from ReportCompletedRel t where t.user_id = ?1 and t.is_completed = 1 and SUBSTR(t.completed_date, 1, 8) < ?2")
	List<ReportCompletedRel> findHisCompletedReport(String userId, String currentDate);
	
}
