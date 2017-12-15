package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QAnswerResult;
import net.riking.entity.model.QuestionAnswer;

/**
 * 
 * 〈问题的回答信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface QuestionAnswerRepo
		extends JpaRepository<QuestionAnswer, String>, JpaSpecificationExecutor<QuestionAnswer> {
	/**
	 * 统计问题的回答数
	 * @param newsCommentId
	 * @return
	 */
	@Query("select count(*) from QuestionAnswer where questionId = ?1 and isAudit <> 2 and isDeleted = 1")
	Integer answerCount(String tqId);

	/**
	 * 回答的列表
	 * @param tqId
	 * @return
	 */
	@Query("select new QuestionAnswer(q.id,q.createdTime,q.modifiedTime,q.userId,q.questionId,q.content,(select a.userName from AppUser a where q.createdBy = a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where q.createdBy = ap.id),(select app.experience from AppUserDetail app where q.createdBy = app.id)) from QuestionAnswer q where q.questionId = ?1 and q.isDeleted=1 and q.isAudit <> 2 order by q.createdTime desc")
	List<QuestionAnswer> findByTqId(String tqId);

	/**
	 * 回答的详情
	 * @param questAnswerId
	 * @return
	 */
	@Query("select new QuestionAnswer(q.id,q.createdTime,q.modifiedTime,q.userId,q.questionId,q.content,(select a.userName from AppUser a where q.createdBy = a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where q.createdBy = ap.id),(select app.experience from AppUserDetail app where q.createdBy = app.id),(select tq.title from TopicQuestion tq where tq.id = q.questionId)) from QuestionAnswer q where q.id = ?1 and q.isDeleted=1 and q.isAudit <> 2")
	QuestionAnswer getById(String questAnswerId);

	/**
	 * 统计用户回答问题的回答数
	 * @param newsCommentId
	 * @return
	 */
	@Query("select count(*) from QuestionAnswer where userId = ?1 and isAudit <> 2 and isDeleted = 1")
	Integer answerCountByUserId(String userId);

	/**
	 * 用户的回答
	 * @param userId
	 * @param pageRequest
	 * @return
	 */
	@Query("select new net.riking.entity.model.QAnswerResult(qa.id,(select tq.id from TopicQuestion tq where tq.id = qa.questionId),(select tq.title from TopicQuestion tq where tq.id = qa.questionId),qa.content,qa.createdTime) from QuestionAnswer qa where qa.userId = ?1 and qa.isAudit <>2 and qa.isDeleted =1")
	List<QAnswerResult> findQAnswerByUserId(String userId, Pageable Pageable);

}
