package net.riking.dao.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ReportSubcribeRel;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:32:39
 * @used TODO
 */
@Repository
public interface RSubscribeRelRepo
		extends JpaRepository<ReportSubcribeRel, String>, JpaSpecificationExecutor<ReportSubcribeRel> {

	@Query("select u.reportId from ReportSubcribeRel u where u.userId = ?1 ")
	Set<String> findbyAppUserId(String appUserId);

	@Query("select u.userId from ReportSubcribeRel u where u.reportId in ?1")
	Set<String> findbyReportId(Set<String> reportId);

	@Query(" from ReportSubcribeRel u where u.userId = ?1")
	List<ReportSubcribeRel> findUserReportList(String appUserId);

	@Query("select t.reportId from ReportSubcribeRel t where t.userId = ?1")
	List<String> findReportByUserId(String userId);

	// 查询用户订阅表未完成的报表id
	@Query("select t.reportId from ReportSubcribeRel t where t.userId = ?1 and t.isComplete=0")
	Set<String> findReportByUserIdAndIsComplete(String userId);

	@Transactional
	@Modifying
	@Query("delete from ReportSubcribeRel b where b.userId = ?1 and b.reportId in(?2)")
	public int deleteReportRel(String userId, String reportId);

	@Query("select t.id,t.userId,t.reportId,t.isComplete from ReportSubcribeRel t where t.userId = ?1 and t.reportId =?2")
	ReportSubcribeRel findByUserIdAndReportId(String userId, String reportId);

	@Transactional
	@Modifying
	@Query("delete from ReportSubcribeRel b where b.userId = ?1 ")
	public int deleteReportRelByUserId(String userId);

	@Transactional
	@Modifying
	@Query("delete from ReportSubcribeRel b where b.userId = ?1 and b.reportId =?2")
	public int deleteReportRelByUserIdAndReportId(String userId, String reportId);

}
