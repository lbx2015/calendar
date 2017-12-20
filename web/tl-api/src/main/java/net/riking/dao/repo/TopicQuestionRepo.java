package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QuestResult;
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
	@Query("select new TopicQuestion(t.id,t.createdTime,t.modifiedTime,t.isAduit,t.title,t.content,t.topicId,t.userId,(select a.userName from AppUser a where t.createdBy = a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where t.createdBy = ap.id),(select app.experience from AppUserDetail app where t.createdBy = app.id)) from TopicQuestion t where t.id = ?1 ")
	TopicQuestion getById(String tqId);

	/**
	 * 根据关键字模糊查询title
	 * @param keyWord
	 * @return
	 */
	@Query("select new net.riking.entity.model.QuestResult(t.id,t.title) from TopicQuestion t where t.isDeleted = 1 and t.isAduit <> 2 and t.title like %?1% ")
	List<QuestResult> getQuestByParam(String keyWord);

	/**
	 * 根据topicId查询话题下面的问题
	 * @param keyWord
	 * @return
	 */
	@Query("select new net.riking.entity.model.QuestResult(tq.id,tq.title,tq.createdTime,(select count(*) from TQuestionRel ttr where ttr.dataType = 0 and ttr.tqId = tq.id) as tqFollowNum,(select count(*) from QuestionAnswer tqa where tqa.questionId = tq.id and tqa.isAduit <> 2 and tqa.isDeleted = 1) as qanswerNum) FROM TopicQuestion tq  where  tq.isAduit <> 2 and tq.isDeleted = 1 and tq.topicId like %?1% ORDER BY tqFollowNum DESC,qanswerNum DESC,tq.createdTime DESC")
	List<QuestResult> findByTid(String topicId, Pageable pageable);

	/**
	 * 查找用户的提问
	 * @param userId,pageable
	 * @return
	 */
	@Query("select new net.riking.entity.model.QuestResult(tq.id,tq.title,tq.createdTime) FROM TopicQuestion tq  where  tq.isAduit <> 2 and tq.isDeleted = 1 and tq.userId = ?1 order by tq.createdTime DESC")
	List<QuestResult> findByUserId(String userId, Pageable pageable);
}
