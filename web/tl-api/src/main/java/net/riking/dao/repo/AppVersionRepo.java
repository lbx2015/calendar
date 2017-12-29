package net.riking.dao.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppVersion;

/**
 * App版本
 * 
 * @author james.you
 * @version crateTime：2017年11月28日 下午2:52:08
 * @used TODO
 */
@Repository
public interface AppVersionRepo extends JpaRepository<AppVersion, String>, JpaSpecificationExecutor<AppVersion> {

	/***
	 * 查询是否有App更新版本
	 * 
	 * @author james.you
	 * @version crateTime：2017年11月28日 下午3:13:44
	 * @used TODO
	 * @param versionNo
	 * @param clientType 1-IOS;2-Android
	 * @return
	 */
	@Query("select new net.riking.entity.model.AppVersion(versionNo, enforce, url, remark) from AppVersion where is_deleted = 1 and versionNo > ?1 and client_type = ?2 and enabled=1 ")
	AppVersion hasUpdateAppVersion(String versionNo, Integer clientType);

	/*********** WEB ************/
	@Transactional
	@Modifying
	@Query(" update AppVersion set isDeleted=0 where id in ?1 ")
	int deleteById(Set<String> ids);

	@Query("from AppVersion where is_deleted = 1 and clientType = ?1")
	List<AppVersion> findMaxVersion(@Param("clientType") Integer clientType);

	@Query("from AppVersion where is_deleted = 1 and versionNo = ?1")
	AppVersion findByAppVersionNO(String versionNo);
}
