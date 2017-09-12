package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppVersion;
/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:32:54
 * @used TODO
 */
@Repository
public interface AppVersionRepo extends JpaRepository<AppVersion, String>, JpaSpecificationExecutor<AppVersion>{
	
	
	@Transactional
	@Modifying
	@Query(" update AppVersion set deleteState = '0' where id in ?1  ")
	int deleteById(Set<String> ids);
	
	@Query(" from AppVersion where deleteState = '1' and versionNumber > ?1 and type = ?2 order by versionNumber desc ")
	List<AppVersion> getByVersionNumber(String versionNumber,String type);
}
