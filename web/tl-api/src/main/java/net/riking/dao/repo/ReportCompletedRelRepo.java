package net.riking.dao.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ReportCompletedRel;
import net.riking.entity.resp.RCompletedRelResp;

@Repository
public interface ReportCompletedRelRepo extends JpaRepository<ReportCompletedRel, String>,
		JpaSpecificationExecutor<ReportCompletedRel>, PagingAndSortingRepository<ReportCompletedRel, String> {

	@Query("select new net.riking.entity.resp.RCompletedRelResp(r.reportId) from ReportCompletedRel r where r.userId = ?1 and r.createdTime BETWEEN ?2 and ?3")
	List<RCompletedRelResp> findNowReport(String userId, Date beginDate, Date endDate);

	// @Query("SELECT t.userId,t.reportId,t.createdTime,l.name, group_concat(c.frequency ORDER BY
	// c.frequency ASC) AS strFrequency FROM ReportCompletedRel t LEFT JOIN Report l ON l.id =
	// t.reportId LEFT JOIN ReportSubmitCaliber c ON t.reportId = c.reportId WHERE t.userId= ?1 AND
	// t.createdTime BETWEEN ?2 and ?3 GROUP BY c.reportId")
	// List<RCompletedRelResp> findCompleteReportByIdAndTime(String userId, Date beginDate, Date
	// endDate);
}
