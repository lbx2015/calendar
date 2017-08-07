package net.riking.service.repo;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.WebVersion;
@Repository
public interface WebVersionRepo extends JpaRepository<WebVersion, String>, JpaSpecificationExecutor<WebVersion> {
	
	/**
	 * @author Lucky.Liu on 2017/8/05.
	 */
	@Transactional
	@Modifying
	@Query(" update WebVersion set deleteState = '0' where id in ?1  ")
	int deleteById(Set<String> ids);
}
