package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QAComment;
import net.riking.entity.model.QACommentResult;

/**
 * 
 * 〈问题的回答的评论信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface QACommentRepo extends JpaRepository<QAComment, String>, JpaSpecificationExecutor<QAComment> {

	/**
	 * 回答的评论数
	 * @param newsId
	 * @return
	 */
	@Query("select count(*) from QAComment where questionAnswerId = ?1 and isAduit <> 2 and isDeleted = 1")
	Integer commentCount(String questionAnswerId);

	/**
	 * 评论列表
	 * @param questAnswerId
	 * @return
	 */
	@Query("select new net.riking.entity.model.QAComment(q.id,q.createdTime,q.modifiedTime,q.isAduit,q.userId,q.questionAnswerId,q.content,(select a.userName from AppUser a where q.userId = a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where q.userId = ap.id),(select app.experience from AppUserDetail app where q.userId = app.id),(select qacId from QACAgreeRel where userId = ?2 and qac_id =q.id  and dataType = 1)) from QAComment q where q.questionAnswerId = ?1 and q.isAduit <> 2 and q.isDeleted=1 order by q.createdTime desc")
	List<QAComment> findByQaId(String questAnswerId, String userId, Pageable pageable);

	/**
	 * 我的评论列表
	 * @param questAnswerId
	 * @return
	 */
	@Query("select new net.riking.entity.model.QACommentResult(q.id,q.createdTime,q.modifiedTime,q.userId,q.questionAnswerId,q.content,(select qa.questionId from QuestionAnswer qa where qa.id = q.questionAnswerId),(select a.userName from AppUser a where q.userId = a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where q.userId = ap.id),(select app.experience from AppUserDetail app where q.userId = app.id)) from QAComment q where q.userId = ?1 and q.isAduit <> 2 and q.isDeleted=1 ORDER BY q.createdTime desc")
	List<QACommentResult> findByUserId(String userId);
}
