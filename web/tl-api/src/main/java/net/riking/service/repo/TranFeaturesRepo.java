package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.TranFeatures;

/**
 * Created by bing.xun on 2017/5/24.
 */
@Repository
public interface TranFeaturesRepo extends JpaRepository<TranFeatures,Long> {
    @Transactional
    @Modifying
    @Query("delete from TranFeatures e where e.id in ?1")
    int deleteByIds(Set<Long> ids);

    List<TranFeatures> findByIdIn(Set<Long> ids);
    
    @Transactional
   	@Modifying
   	@Query("update TranFeatures set approved=1 where id in ?1")
   	Integer approveMore(Set<Long> ids);

   	@Transactional
   	@Modifying
   	@Query("update TranFeatures set approved=0 where id in ?1")
   	Integer cancelApproveMore(Set<Long> ids);

   	List<TranFeatures> findByApproved(Integer approved);
}
