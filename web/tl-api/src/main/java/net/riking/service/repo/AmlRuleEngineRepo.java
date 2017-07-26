package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AmlRuleEngine;

@Repository
public interface AmlRuleEngineRepo extends JpaRepository<AmlRuleEngine, Long> , JpaSpecificationExecutor<AmlRuleEngine>{

	@Query("from AmlRuleEngine where enabled = ?1 and type = ?2 and khlx in ?3 and approved =1")
	List<AmlRuleEngine> findAmlRuleEngineByEnabledandTypeAndKhlxIn(Integer ena,Integer type,Set<Byte> khlx);
	
	List<AmlRuleEngine> findByType(int type);
	
	List<AmlRuleEngine> findByRuleNo(String ruleNo);
	
	@Transactional
	@Modifying
	@Query("update AmlRuleEngine set approved=1 where id in ?1")
	Integer approveMore(Set<Long> ids);

	@Transactional
	@Modifying
	@Query("update AmlRuleEngine set approved=0 where id in ?1")
	Integer cancelApproveMore(Set<Long> ids);

	@Transactional
	@Modifying
	@Query("delete from AmlRuleEngine e where e.id in ?1 and approved=0")
	Integer deleteMore(Set<Long> ids);
}
