package net.riking.service.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUserReportRel;
@Repository
public interface AppUserReportRepo  extends JpaRepository<AppUserReportRel, String>, JpaSpecificationExecutor<AppUserReportRel> {
	
	/**
	 * @author Lucky.Liu on 2017/8/05.
	 */
	
	@Query("select reportId from AppUserReportRel u where u.appUserId = ?1 and deleteState= '1' ")
	Set<String> findbyAppUserId(String appUserId);
}
