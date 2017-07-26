package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.BaseAif;

@Repository
public interface BaseAifRepo extends JpaRepository<BaseAif, Long>, JpaSpecificationExecutor<BaseAif> {
	
	@Query("from BaseAif a  where a.khbh = ?1 and a.id <> ?2 and enabled = '1'")
	List<BaseAif> findByKhbhAndNotId(String khbh ,Long id );
	
	@Modifying
	@Transactional
	@Query("update BaseAif a set  a.enabled = '0' where  a.id in ?1 and confirmStatus='101001'")
	Integer delById(Set<Long> id);
	
	BaseAif findByZhAndEnabledAndConfirmStatus(String zh ,String enabl,String confirmStatus );
	
	@Query("select zh from BaseAif a  where a.zh like ?1 and a.confirmStatus ='101002' and enabled = '1' group by id,zh")
	List<String> findLikeZh(String zh);
	
	@Query("from BaseAif a  where a.syncLastTime >= ?1 and a.confirmStatus ='101002' and enabled = '1' ")
	List<BaseAif> findBySyncLastTime(Long syncLastTime);
}
