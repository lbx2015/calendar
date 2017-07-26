package net.riking.service.repo;

import net.riking.entity.model.AmlRuleEngine;
import net.riking.entity.model.AmlRuleEngineScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

/**
 * Created by bing.xun on 2017/7/4.
 */
@Repository
public interface AmlRuleEngineScoreRepo extends JpaRepository<AmlRuleEngineScore, Long> {


    @Query("select ruleEngineId from AmlRuleEngineScore where tranFeaturesId =?1")
    List<Long> getByTranFeaturesId(Long tranFeaturesId);

    List<AmlRuleEngineScore> findByTranFeaturesId(Long tranFeaturesId);
    
    @Query("select id from AmlRuleEngineScore where ruleEngineId = ?1")
    List<Long> getByRuleEngineId(Long ruleEngineId);
    
    AmlRuleEngineScore findByRuleEngineIdAndTranFeaturesId(Long ruleEngineId,Long tranFeaturesId);

    List<AmlRuleEngine> findByIdIn(Set<Long> ids);
    
    @Transactional
	@Modifying
    @Query("delete from AmlRuleEngineScore where tranFeaturesId  in ?1 ")
    Integer deleteByTranFeaturesId(Set<Long> ids);
    
}
