package net.riking.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.NCommentReplyInfo;

/**
 * 
 * 〈行业资讯评论回复信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NCommentReplyInfoRepo
		extends JpaRepository<NCommentReplyInfo, String>, JpaSpecificationExecutor<NCommentReplyInfo> {

	/**
	 * 
	 * @param newsId
	 * @return
	 */
	@Query("select n.id,n.userId,n.toUserId,n.newsCommentId,n.nCommentReplyId,n.content from NCommentReplyInfo n where enabled = 1 order by createdTime desc")
	List<NCommentReplyInfo> findByNewsCommentId(String newsCommentId);
}
