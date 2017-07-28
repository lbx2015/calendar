package net.riking.service.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUserReportRel;
@Repository
public interface AppUserReportRepo  extends JpaRepository<AppUserReportRel, String>, JpaSpecificationExecutor<AppUserReportRel> {
	
	@Query("select reportId from AppUserReport u where u.appUserId = ?1 and deleteState= '1' and enabled = '1' ")
	Set<String> findbyAppUserId(String appUserId);
}
