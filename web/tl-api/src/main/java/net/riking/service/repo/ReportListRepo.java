package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QueryReport;
import net.riking.entity.model.ReportList;
/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:33:38
 * @used TODO
 */
@Repository
public interface ReportListRepo extends JpaRepository<ReportList, String>, JpaSpecificationExecutor<ReportList> {


	@Query(" from ReportList  where Id in ?1 and deleteState = '1'  ")
	List<ReportList> findByReoprtId(Set<String> reoprtId);
	
	@Query("select new net.riking.entity.model.QueryReport(r.id,r.reportName,r.reportCode,r.moduleType) from ReportList r where r.deleteState='1' ")
	List<QueryReport> findByDeleteState();
	
	@Transactional
	@Modifying
	@Query(" update ReportList set deleteState = '0' where id in ?1  ")
	int deleteById(Set<String> ids);
	
	@Query("  select new net.riking.entity.model.QueryReport(r.id,r.reportName,r.reportCode,r.moduleType) from ReportList r where r.deleteState='1' and  r.id in ?1 ")
	List<QueryReport> findByIds(Set<String> ids);
}
