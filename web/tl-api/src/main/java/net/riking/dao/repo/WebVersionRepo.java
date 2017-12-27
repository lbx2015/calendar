package net.riking.dao.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.WebVersion;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:34:02
 * @used TODO
 */
@Repository
public interface WebVersionRepo extends JpaRepository<WebVersion, String>, JpaSpecificationExecutor<WebVersion> {

	/*********** WEB ************/
	@Transactional
	@Modifying
	@Query(" update WebVersion set isDeleted=0 where id in ?1 ")
	int deleteById(Set<String> ids);

	@Query(" from WebVersion where isDeleted=1 ")
	List<WebVersion> findMaxVersion();

	@Query(" from WebVersion where isDeleted=1 and versionNo=?1")
	WebVersion findOneByVersionNO(String versionNo);
}
