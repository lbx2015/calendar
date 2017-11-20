package net.riking.service.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.TopicFollow;

@Repository
public interface TopicFollowRepo extends JpaRepository<TopicFollow, Long>, JpaSpecificationExecutor<TopicFollow>{
	
	@Query(" select t.topicId from TopicFollow t where t.userId =?")
	Set<String> findByUserId(String userId);

}
