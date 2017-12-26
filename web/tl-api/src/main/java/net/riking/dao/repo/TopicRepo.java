package net.riking.dao.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Topic;
import net.riking.entity.model.TopicResult;

/**
 * 
 * 〈话题信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface TopicRepo extends JpaRepository<Topic, String>, JpaSpecificationExecutor<Topic> {
	/**
	 *
	 * @param topicId
	 * @return
	 */
	@Query("select new net.riking.entity.model.Topic(t.id,t.createdTime,t.modifiedTime,t.isAduit,t.title,t.content,t.topicUrl) from Topic t where t.id = ?1 and t.isAduit <> 2 and t.isDeleted = 1")
	Topic getById(String topicId);

	/**
	 *
	 * @param topicId
	 * @return
	 */
	@Query("select new net.riking.entity.model.TopicResult(t.id,t.title,t.content,(select topicId from TopicRel where userId = ?1 and t.id = topic_id and dataType = 0)) from Topic t where t.isAduit <> 2 and t.isDeleted = 1 and t.title like %?1%")
	List<TopicResult> getTopicByParam(String params);

	/*********** WEB ************/
	@Transactional
	@Modifying
	@Query(" update Topic set isDeleted=0 where id in ?1 ")
	int deleteById(Set<String> ids);

	@Transactional
	@Modifying
	@Query(" update Topic set isAduit=1 where id in ?1 ")
	int verifyById(Set<String> ids);
}
