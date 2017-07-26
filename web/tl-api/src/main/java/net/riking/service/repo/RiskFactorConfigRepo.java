package net.riking.service.repo;


import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.RiskFactorConfig;
@Repository
public interface RiskFactorConfigRepo extends JpaRepository<RiskFactorConfig, Long>{

	@Query("from  RiskFactorConfig r where r.factorCode in ?1 ")
	List<RiskFactorConfig> getConfigs(Set<String> factorCode);
	
	@Query("from  RiskFactorConfig r where r.factorCode = ?1 and r.factorCodeName=?2")
	RiskFactorConfig getRiskFactorConfig(String factorCode ,String factorCodeName);
	
	@Query("select factorCodeName from RiskFactorConfig")
	List<String> getAllConfig();
	
	@Query("select score from RiskFactorConfig c where c.factorCodeName = ?1")
	String getScoreByConfig(String factorCodeName);
	
	@Transactional
	@Modifying
	@Query("delete from RiskFactorConfig where factorCode = ?1")
	void deleteByFactorCode(String factorCode);

}
