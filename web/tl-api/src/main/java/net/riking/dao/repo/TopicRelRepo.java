package net.riking.dao.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.TopicRel;

/**
 * 
 * 〈话题关注信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface TopicRelRepo extends JpaRepository<TopicRel, String>, JpaSpecificationExecutor<TopicRel> {
	/**
	 * 话题取消关注
	 * @param userId
	 * @param newsId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("delete TopicRel where userId =?1 and topicId = ?2 and dataType = ?3")
	void deleteByUIdAndTopId(String userId, String topicId, Integer dataType);

}
