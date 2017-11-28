package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.TopicQuestion;

/**
 * 
 * 〈话题的问题信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface TopicQuestionRepo
		extends JpaRepository<TopicQuestion, String>, JpaSpecificationExecutor<TopicQuestion> {
	/**
	 * 
	 * @param tqId
	 * @return
	 */
	@Query("select new TopicQuestion(t.id,t.createdTime,t.modifiedTime,t.isAduit,t.title,t.content,t.topicId,t.userId,(select a.userName from AppUser a where t.createdBy = a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where t.createdBy = ap.id)) from TopicQuestion t where t.id = ?1 ")
	TopicQuestion getById(String tqId);
}
