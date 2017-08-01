package net.riking.service.repo;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.CtryHdayCrcy;

@Repository
public interface CtryHdayCrcyRepo extends JpaRepository<CtryHdayCrcy, String>, JpaSpecificationExecutor<CtryHdayCrcy>{
	@Transactional
	@Modifying
	@Query("update CtryHdayCrcy set deleteState = '0'  where id in ?1")
	public int deleteByIds(Set<String> ids);
}