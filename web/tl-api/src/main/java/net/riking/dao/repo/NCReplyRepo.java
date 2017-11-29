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
	@Query("select new net.riking.entity.model.NCReply(nc.id,nc.createdTime,nc.modifiedTime,nc.userId,nc.toUserId,nc.commentId,nc.replyId,nc.content,(select au.userName from AppUser au where nc.userId = au.id and au.isDeleted=1),(select app.photoUrl from AppUserDetail app where nc.userId = app.id)) from NCReply nc where nc.commentId =?1 and nc.isAduit <> 2 and nc.isDeleted = 1 order by nc.createdTime desc")
	List<NCReply> findByNewsCommentId(String newsCommentId);
}
