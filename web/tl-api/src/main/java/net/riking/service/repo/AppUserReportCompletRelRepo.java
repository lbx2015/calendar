package net.riking.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUserReportCompleteRel;
import net.riking.entity.model.QureyResulte;

@Repository
public interface AppUserReportCompletRelRepo extends JpaRepository<AppUserReportCompleteRel, String>, JpaSpecificationExecutor<AppUserReportCompleteRel> {

	@Query("select new net.riking.entity.model.QureyResulte(a.reportId) from AppUserReportCompleteRel a where a.appUserId = ?1 and a.completeDate=?2 ")
	List<QureyResulte> getReportId(String userId, String date);
}
