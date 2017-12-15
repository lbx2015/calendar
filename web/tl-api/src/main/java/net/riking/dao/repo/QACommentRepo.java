package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QAComment;

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
	@Query("select count(*) from QAComment where questionAnswerId = ?1 and isAudit <> 2 and isDeleted = 1")
	Integer commentCount(String questionAnswerId);

	/**
	 * 评论列表
	 * @param questAnswerId
	 * @return
	 */
	@Query("select new net.riking.entity.model.QAComment(q.id,q.createdTime,q.modifiedTime,q.isAudit,q.userId,q.questionAnswerId,q.content,(select a.userName from AppUser a where q.userId = a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where q.userId = ap.id),(select app.experience from AppUserDetail app where q.userId = app.id)) from QAComment q where q.questionAnswerId = ?1 and q.isAudit <> 2 and q.isDeleted=1 order by q.createdTime desc")
	List<QAComment> findByQaId(String questAnswerId);

}
