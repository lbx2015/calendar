package net.riking.service.repo;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QueryReport;
import net.riking.entity.model.ReportSubmitCaliber;
@Repository
public interface ReportSubmitCaliberRepo extends JpaRepository<ReportSubmitCaliber, String>, JpaSpecificationExecutor<ReportSubmitCaliber>{

	@Transactional
	@Modifying
	@Query("update ReportSubmitCaliber set deleteState = '0'  where id in ?1")
	int deleteByIds(Set<String> ids);
	
	@Transactional
	@Modifying
	@Query("update ReportSubmitCaliber set enabled = 1  where id = ?1")
	int enable(String id);
	
	@Transactional
	@Modifying
	@Query("update ReportSubmitCaliber set enabled = 0  where id = ?1")
	int unEnable(String id);
	
	@Query("select r.reportId from ReportSubmitCaliber r where ((r.frequency='1') or (r.frequency='2' and r.delayDates>=?1) or (r.frequency='3' and r.delayDates>=?2) or (r.frequency='4' and r.delayDates>=?3) or(r.frequency='5' and r.delayDates>=?4) or (r.frequency='6' and r.delayDates>=?5) or (r.frequency='7' and r.delayDates>=?6)) and r.isWorkDay=1 and r.enabled = 1")
	Set<String> findByWorkDatefromReportId(Integer week,Integer ten,Integer month,Integer season,Integer halfYear,Integer Year);
	
	@Query("select r.reportId from ReportSubmitCaliber r where ((r.frequency='1') or (r.frequency='2' and r.delayDates>=?1) or (r.frequency='3' and r.delayDates>=?2) or (r.frequency='4' and r.delayDates>=?3) or(r.frequency='5' and r.delayDates>=?4) or (r.frequency='6' and r.delayDates>=?5) or (r.frequency='7' and r.delayDates>=?6)) and r.isWorkDay=0 and r.enabled = 1")
	Set<String> findByFreeDatefromReportId(Integer week,Integer ten,Integer month,Integer season,Integer halfYear,Integer Year);

}
