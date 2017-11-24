package net.riking.service.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.NewsCommentInfo;

/**
 * 
 * 〈行业资讯评论信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NewsCommentInfoRepo
		extends JpaRepository<NewsCommentInfo, String>, JpaSpecificationExecutor<NewsCommentInfo> {

	@Query("select count(*) from NewsCommentInfo where newsId = ?1 and enabled = 1")
	Integer commentCount(String newsId);

	/**
	 * 
	 * @param newsId
	 * @return
	 */
	@Query("select n.id,n.createdTime,n.userId,n.newsId,n.content from NewsCommentInfo n where createdTime < ?1 and enabled = 1 order by createdTime desc")
	List<NewsCommentInfo> findByNewsId(String newsId, Pageable pageable);
}
