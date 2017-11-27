package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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

	// @Transactional
	// @Modifying
	// @Query(" update WebVersion set deleteState = '0' where id in ?1 ")
	// int deleteById(Set<String> ids);
}
