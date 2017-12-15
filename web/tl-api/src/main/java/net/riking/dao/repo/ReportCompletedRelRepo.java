package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ReportCompletedRel;

@Repository
public interface ReportCompletedRelRepo extends JpaRepository<ReportCompletedRel, String>,
		JpaSpecificationExecutor<ReportCompletedRel>, PagingAndSortingRepository<ReportCompletedRel, String> {

	@Query("select r.reportId from ReportCompletedRel r where r.userId = ?1 and r.completedDate = ?2")
	List<String> findNowReport(String userId, String completedDate);

}
