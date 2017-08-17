package net.riking.service.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUserReportRel;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:32:39
 * @used TODO
 */
@Repository
public interface AppUserReportRelRepo  extends JpaRepository<AppUserReportRel, String>, JpaSpecificationExecutor<AppUserReportRel> {

	
	@Query("select reportId from AppUserReportRel u where u.appUserId = ?1 and deleteState= '1' ")
	Set<String> findbyAppUserId(String appUserId);
}
