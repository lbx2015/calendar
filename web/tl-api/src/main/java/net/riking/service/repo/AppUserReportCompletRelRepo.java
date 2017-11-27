package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ReportCompletedRel;

@Repository
public interface AppUserReportCompletRelRepo extends JpaRepository<ReportCompletedRel, String>,
		JpaSpecificationExecutor<ReportCompletedRel>, PagingAndSortingRepository<ReportCompletedRel, String> {

	// @Query("select new net.riking.entity.model.QureyResulte(a.reportId) from
	// AppUserReportCompleteRel a where a.appUserId = ?1 and a.completeDate=?2 ")
	// List<QureyResulte> getReportId(String userId, String date);

}
