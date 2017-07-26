package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.BaseCorpCust;

@Repository
public interface BaseCorpCustRepo extends JpaRepository<BaseCorpCust, Long>, JpaSpecificationExecutor<BaseCorpCust> {
	
	@Query("from BaseCorpCust  where khzjhm like ?1  and enabled = '1' and confirmStatus = '101002'")
	List<BaseCorpCust> findKhzjhmLike(String khzjhm);
	
	BaseCorpCust findByIdAndKhzjhm(Long id,String khzjhm);
	
	@Query("from BaseCorpCust where khbh = ?1 and enabled = '1' and confirmStatus = '101002'")
	BaseCorpCust findByKhbhToOne(String khbh);
	
	@Query("from BaseCorpCust where khzjhm = ?1 and enabled = '1' and confirmStatus = '101002'")
	BaseCorpCust findByKhzjhmToOne(String khzjhm);
	
	@Query("from BaseCorpCust where khbh like ?1 ")
	List<BaseCorpCust> findByKhbh(String khbh);
	
	@Query("from BaseCorpCust a  where a.khbh = ?1 and a.id <> ?2 and enabled = '1'")
	List<BaseCorpCust>findByKhbhAndNotId(String khbh,Long id);
	
	@Modifying
	@Transactional
	@Query("update BaseCorpCust a set  a.enabled = '0' where  a.id in ?1 and confirmStatus='101001'")
	Integer delById(Set<Long> id);
	
	@Modifying
	@Transactional
	@Query("update BaseCorpCust a set  a.jobId = ?1,a.confirmStatus=?2  where  a.id = ?3")
	Integer updateJobIdById(String jobId,String confirmStatus, Long id);

	@Modifying
	@Transactional
	@Query("update BaseCorpCust r set r.riskTime = ?2 where r.id = ?1")
	int setRiskTime(Long id,String riskTime);
	
	List<BaseCorpCust> findByKhbhAndEnabledAndIdNot(String ticd,String enabled,Long id);
	
	List<BaseCorpCust> findByKhzjhmAndEnabledAndIdNot(String ticd,String enabled,Long id);
	
	List<BaseCorpCust> findByYhzhAndEnabledAndIdNot(String ticd,String enabled,Long id);
	
	List<BaseCorpCust> findByNsrsbmAndEnabledAndIdNot(String ticd,String enabled,Long id);
	
	@Query("from BaseCorpCust r where r.confirmStatus='101002' and r.enabled='1' and id in?1")
	List<BaseCorpCust> findByides(Set<Long> ides);
	
	@Query("from BaseCorpCust a  where a.syncLastTime >= ?1 and a.confirmStatus ='101002' and enabled = '1' ")
	List<BaseCorpCust> findBySyncLastTime(Long syncLastTime);
	
	
}
