package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.TopicRel;

/**
 * 
 * 〈话题信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface TopicFollowInfoRepo
		extends JpaRepository<TopicRel, String>, JpaSpecificationExecutor<TopicRel> {

}
