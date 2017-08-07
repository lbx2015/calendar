package net.riking.service.repo;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppVersion;
@Repository
public interface AppVersionRepo extends JpaRepository<AppVersion, String>, JpaSpecificationExecutor<AppVersion>{
	
	/**
	 * @author Lucky.Liu on 2017/8/05.
	 */
	
	@Transactional
	@Modifying
	@Query(" update AppVersion set deleteState = '0' where id in ?1  ")
	int deleteById(Set<String> ids);
	
	
	AppVersion findFirstByDeleteStateOrderByRenewalTime(String deletestate);
}
