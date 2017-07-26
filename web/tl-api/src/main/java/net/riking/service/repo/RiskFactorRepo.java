package net.riking.service.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.risk.RiskFactor;

@Repository
public interface RiskFactorRepo extends JpaRepository<RiskFactor, Long>{

	RiskFactor findById(Long id);
	
	@Transactional
	@Modifying
	@Query("update RiskFactor r set r = ?1")
	int update(RiskFactor riskFactor);
	
	@Query("select weights from RiskFactor c where c.corptrnProp = ?1")
	String getWeights(String corptrnProp);
	
	@Query("from RiskFactor c where c.risktype = '1' ")
	List<RiskFactor> getCorpCust();
	
	@Query("from RiskFactor c where c.risktype = '2' ")
	List<RiskFactor> getIndvCust();

	@Query("From RiskFactor")
	List<RiskFactor> findMore();
	
	Page<RiskFactor> findByRisktypeAndParentFactorCodeNot(String risktype, String ParentFactorCode,Pageable pageable);
}
