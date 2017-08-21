package net.riking.service.repo;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
	
}
