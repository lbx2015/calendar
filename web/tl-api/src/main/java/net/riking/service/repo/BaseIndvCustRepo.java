package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.BaseIndvCust;

@Repository
public interface BaseIndvCustRepo extends JpaRepository<BaseIndvCust, Long>, JpaSpecificationExecutor<BaseIndvCust> {
	
	@Query("from BaseIndvCust  where zjhm like ?1 and enabled = '1' and confirmStatus = '101002'")
	List<BaseIndvCust> findZjhmLike(String zjhm);
	
	BaseIndvCust findByIdAndZjhm(Long id,String zjhm);
	
	@Query("from BaseIndvCust where khbh = ?1 and enabled = '1' and confirmStatus = '101002'")
	BaseIndvCust findByKhbhToOne(String khbh);
	
	@Query("from BaseIndvCust  where zjhm = ?1 and enabled = '1' and confirmStatus = '101002'")
	BaseIndvCust findByKhzjhmToOne(String khzjhm);
	
	@Query("from BaseIndvCust where khbh like ?1 ")
	List<BaseIndvCust> findByKhbh(String khbh);
	
	@Modifying
	@Transactional
	@Query("update BaseIndvCust a set  a.enabled = '0' where  a.id in ?1 and confirmStatus='101001'")
	Integer delById(Set<Long> id);
	
	@Query("from BaseIndvCust a  where a.khbh = ?1 and a.id <> ?2 and enabled = '1'")
	List<BaseIndvCust>findByKhbhAndNotId(String khbh,Long id);

	@Modifying
	@Transactional
	@Query("update BaseIndvCust r set r.riskTime = ?2 where r.id = ?1")
	int setRiskTime(Long id,String riskTime);
	
	@Query("from BaseIndvCust r where r.confirmStatus='101002' and r.enabled='1' and id in?1")
	List<BaseIndvCust> findByides(Set<Long> ides);
	
	@Query("from BaseIndvCust a  where a.syncLastTime >= ?1 and a.confirmStatus ='101002' and enabled = '1' ")
	List<BaseIndvCust> findBySyncLastTime(Long syncLastTime);
	
}
