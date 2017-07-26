package net.riking.service.repo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.BaseTrn;

@Repository
public interface BaseTrnRepo extends JpaRepository<BaseTrn, Long>, JpaSpecificationExecutor<BaseTrn> {
	
	@Modifying
	@Transactional
	@Query("update BaseTrn a set  a.enabled = 0, a.delState = '0'  where a.jylsh = ?1 and a.id <> ?2")
	Integer delByJylshAndNotId(String jylsh,Long id);
	
	@Modifying
	@Transactional
	@Query("update BaseTrn a set  a.enabled = 0, a.delState = '0'  where  a.id in ?1 and confirmStatus='101001'")
	Integer delById(Set<Long> id);
	
	@Query("from BaseTrn  where  id in ?1")
	List<BaseTrn> getByIds(Set<Long> id);
	
	@Query("from BaseTrn a  where a.jylsh = ?1 and a.id <> ?2 and delState = '1'")
	List<BaseTrn> findByJylshAndNotId(String jylsh,Long id);
	
	@Query("from BaseTrn a  where a.jylsh = ?1 and a.jyrq=?2 and delState =?3 ")
	BaseTrn findByJylshAndJyrqAndEnabled(String jylsh,Date jyrq,String enabled);
	
	@Query("from BaseTrn a  where a.syncLastTime >= ?1 and a.confirmStatus ='101002' and enabled = '1' and delState = '1' ")
	List<BaseTrn> findBySyncLastTime(Long syncLastTime);
}
