package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Report;
import net.riking.entity.model.ReportSubscribeRel;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:33:38
 * @used TODO
 */
@Repository
public interface ReportRepo extends JpaRepository<Report, String>, JpaSpecificationExecutor<Report> {

	// @Query(" from ReportList where Id in ?1 and deleteState = '1' ")
	// List<Report> findByReoprtId(Set<String> reoprtId);
	//
	// @Query("select new
	// net.riking.entity.model.QueryReport(r.id,r.reportName,r.reportCode,r.moduleType) from
	// ReportList r where r.deleteState='1' ")
	// List<QueryReport> findByDeleteState();
	//
	// @Transactional
	// @Modifying
	// @Query(" update ReportList set deleteState = '0' where id in ?1 ")
	// int deleteById(Set<String> ids);
	//
	// @Query(" select new
	// net.riking.entity.model.QueryReport(r.id,r.reportName,r.reportCode,r.moduleType) from
	// ReportList r where r.deleteState='1' and r.id in ?1 ")
	// List<QueryReport> findByIds(Set<String> ids);
	//
	// @Query(" select new net.riking.entity.model.AppUserReportRel(r.id) from ReportList r where
	// r.deleteState='1' ")
	// List<ReportSubcribeRel> findAllId();

	@Query("SELECT new net.riking.entity.model.ReportSubscribeRel(t.reportId,(select r.title from Report r where t.reportId=r.id)) FROM ReportSubscribeRel t WHERE t.userId=?1")
	List<ReportSubscribeRel> findByUserId(String userId);
}
