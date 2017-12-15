package net.riking.dao.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ReportSubscribeRel;
import net.riking.entity.model.ReportSubscribeRel;

/**
 * 用户报表订阅表
 * @author jc.tan 2017年12月1日
 * @see
 * @since 1.0
 */
@Repository
public interface ReportSubscribeRelRepo
		extends JpaRepository<ReportSubscribeRel, String>, JpaSpecificationExecutor<ReportSubscribeRel> {

	@Query("select u.reportId from ReportSubscribeRel u where u.userId = ?1 ")
	List<String> findByUserId(String userId);

//	@Query("select u.userId from ReportSubscribeRel u where u.reportId in ?1")
//	Set<String> findbyReportId(Set<String> reportId);
	
	/**
	 * 获取用户订阅的报表
	 * @author james.you
	 * @version crateTime：2017年12月12日 下午5:57:04
	 * @used TODO
	 * @param userId
	 * @return
	 */
	@Query("select new net.riking.entity.model.ReportSubscribeRel(a.reportId, "
			+ "(select t.code from Report t where t.id=a.reportId)) "
			+ "from ReportSubscribeRel a  where a.userId = ?1 ")
	List<ReportSubscribeRel> findSubscribeReportList(String userId);

//	@Query("select t.reportId from ReportSubscribeRel t where t.userId = ?1")
//	List<String> findReportByUserId(String userId);

	// 查询用户订阅表未完成的报表id
//	@Query("select t.reportId from ReportSubscribeRel t where t.userId = ?1 and t.isComplete=0")
//	Set<String> findReportByUserIdAndIsComplete(String userId);


//	@Query("select new net.riking.entity.model.ReportSubscribeRel(t.userId,t.reportId,t.isComplete,t.createdTime) from ReportSubscribeRel t where t.userId = ?1 and t.reportId =?2")
//	ReportSubscribeRel findByUserIdAndReportId(String userId, String reportId);

	@Transactional
	@Modifying
	@Query("delete from ReportSubscribeRel where user_id = ?1 and reportId not in (?2) ")
	public int deleteNotSubscribeByUserId(String userId, String[] reportIds);

//	@Transactional
//	@Modifying
//	@Query("delete from ReportSubscribeRel b where b.userId = ?1 and b.reportId =?2")
//	public int deleteReportRelByUserIdAndReportId(String userId, String reportId);

}
