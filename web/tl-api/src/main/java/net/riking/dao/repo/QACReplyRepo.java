package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QACReply;

/**
 * 问题评论的回复
 * @author jc.tan 2017年12月2日
 * @see
 * @since 1.0
 */
@Repository
public interface QACReplyRepo extends JpaRepository<QACReply, String>, JpaSpecificationExecutor<QACReply> {
	/**
	 * 回复列表
	 * @param questAnswerId
	 * @return
	 */
	@Query("select new QACReply(qr.id,qr.createdTime,qr.isAduit,qr.fromUserId,qr.toUserId,qr.commentId,qr.replyId,qr.content,(select a.userName from AppUser a where qr.fromUserId =a.id),(select au.userName from AppUser au where qr.toUserId = au.id)) from QACReply qr where qr.commentId = ?1 order by createdTime desc")
	List<QACReply> getByCommentId(String commentId);

}
