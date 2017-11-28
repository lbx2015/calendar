package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.NCReply;

/**
 * 
 * 〈行业资讯评论回复信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NCReplyRepo extends JpaRepository<NCReply, String>, JpaSpecificationExecutor<NCReply> {

	/**
	 * 资讯回复列表
	 * @param newsCommentId
	 * @return
	 */
	@Query("select new net.riking.entity.model.NCReply(n.id,n.createdTime,n.userId,n.toUserId,n.commentId,n.replyId,n.content,(select a.userName from AppUser a where n.userId = a.id and a.isDeleted=1) as userName) from NCReply n where n.commentId =?1 and n.isAduit <> 2 and n.isDeleted = 1 order by n.createdTime desc")
	List<NCReply> findByNewsCommentId(String newsCommentId);
}
