package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.NCommentAgreeInfo;

/**
 * 
 * 〈行业资讯评论点赞信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NCommentAgreeInfoRepo
		extends JpaRepository<NCommentAgreeInfo, String>, JpaSpecificationExecutor<NCommentAgreeInfo> {
	/**
	 * 
	 * @param newsCommentId
	 * @return
	 */
	@Query("select count(*) from NCommentAgreeInfo where newsCommentId = ?1 and enabled = 1")
	Integer commentCount(String newsCommentId);
}
