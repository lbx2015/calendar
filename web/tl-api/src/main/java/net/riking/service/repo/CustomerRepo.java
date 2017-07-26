package net.riking.service.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long>,CustomerDao, JpaSpecificationExecutor<Customer>{
	

	
	@Transactional
	@Modifying
	@Query("update Customer r set r.riskTime = ?2 where r.id = ?1")
	int setRiskTime(Long id,String riskTime);
	
	@Transactional
	@Modifying
	@Query("update Customer r set r.riskRank = ?2 where r.id = ?1")
	int setRiskRank(Long id,String riskRank);
	
	@Transactional
	@Modifying
	@Query("update Customer set riskTime = ?1")
	int setAllTime(String riskTime);

	Customer findByCsnm(String csnm);
	
}
