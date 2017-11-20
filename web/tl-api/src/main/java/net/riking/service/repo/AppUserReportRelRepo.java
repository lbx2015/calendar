package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUserReportRel;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:32:39
 * @used TODO
 */
@Repository
public interface AppUserReportRelRepo  extends JpaRepository<AppUserReportRel, String>, JpaSpecificationExecutor<AppUserReportRel> {

	
	@Query("select reportId from AppUserReportRel u where u.appUserId = ?1 ")
	Set<String> findbyAppUserId(String appUserId);
	
	@Query("select u.appUserId from AppUserReportRel u where u.reportId in ?1")
	Set<String> findbyReportId(Set<String> reportId);
	
	@Query(" from AppUserReportRel u where u.appUserId = ?1")
	List<AppUserReportRel> findUserReportList(String appUserId);
	
	@Query("select t.reportId from AppUserReportRel t where t.appUserId = ?1")
	List<String> findReportByUserId(String userId);
	
	//查询用户订阅表未完成的报表id
	@Query("select t.reportId from AppUserReportRel t where t.appUserId = ?1 and t.isComplete=0")
	Set<String> findReportByUserIdAndIsComplete(String userId);
	
	@Transactional
	@Modifying
	@Query("delete from AppUserReportRel b where b.appUserId = ?1 and b.reportId in(?2)")
	public int deleteReportRel(String userId,String reportId);
	
	@Query(" from AppUserReportRel t where t.appUserId = ?1 and t.reportId =?2")
	AppUserReportRel findByUserIdAndReportId(String userId,String reportId);
	
	@Transactional
	@Modifying
	@Query("delete from AppUserReportRel b where b.appUserId = ?1 ")
	public int deleteReportRelByUserId(String userId);
	
}
