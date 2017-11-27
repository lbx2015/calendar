package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ReportSubcribeRel;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:32:39
 * @used TODO
 */
@Repository
public interface AppUserReportRelRepo
		extends JpaRepository<ReportSubcribeRel, String>, JpaSpecificationExecutor<ReportSubcribeRel> {
	// TODO 暫時注釋
	// @Query("select reportId from AppUserReportRel u where u.appUserId = ?1 ")
	// Set<String> findbyAppUserId(String appUserId);
	//
	// @Query("select u.appUserId from AppUserReportRel u where u.reportId in ?1")
	// Set<String> findbyReportId(Set<String> reportId);
	//
	// @Query(" from AppUserReportRel u where u.appUserId = ?1")
	// List<ReportSubcribeRel> findUserReportList(String appUserId);
	//
	// @Query("select t.reportId from AppUserReportRel t where t.appUserId = ?1")
	// List<String> findReportByUserId(String userId);
	//
	// //查询用户订阅表未完成的报表id
	// @Query("select t.reportId from AppUserReportRel t where t.appUserId = ?1 and t.isComplete=0")
	// Set<String> findReportByUserIdAndIsComplete(String userId);
	//
	// @Transactional
	// @Modifying
	// @Query("delete from AppUserReportRel b where b.appUserId = ?1 and b.reportId in(?2)")
	// public int deleteReportRel(String userId,String reportId);
	//
	// @Query("select t.id,t.appUserId,t.reportId,t.isComplete from AppUserReportRel t where
	// t.appUserId = ?1 and t.reportId =?2")
	// ReportSubcribeRel findByUserIdAndReportId(String userId,String reportId);
	//
	// @Transactional
	// @Modifying
	// @Query("delete from AppUserReportRel b where b.appUserId = ?1 ")
	// public int deleteReportRelByUserId(String userId);
	//
	// @Transactional
	// @Modifying
	// @Query("delete from AppUserReportRel b where b.appUserId = ?1 and b.reportId =?2")
	// public int deleteReportRelByUserIdAndReportId(String userId,String reportId);

}
