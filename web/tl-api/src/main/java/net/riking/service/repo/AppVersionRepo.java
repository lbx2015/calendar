package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppVersion;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:32:54
 * @used TODO
 */
@Repository
public interface AppVersionRepo extends JpaRepository<AppVersion, String>, JpaSpecificationExecutor<AppVersion> {

	// TODO 暂时注释
	// @Transactional
	// @Modifying
	// @Query(" update AppVersion set deleteState = '0' where id in ?1 ")
	// int deleteById(Set<String> ids);
	// TODO 暂时注释
	// @Query(" from AppVersion where deleteState = '1' and versionNumber > ?1 and type = ?2 order
	// by versionNumber desc ")
	// List<AppVersion> getByVersionNumber(String versionNumber,String type);
}
