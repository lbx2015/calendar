package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.NewsComment;

/**
 * 
 * 〈行业资讯评论信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NewsCommentInfoRepo extends JpaRepository<NewsComment, String>, JpaSpecificationExecutor<NewsComment> {
	// /**
	// * 统计资讯评论数
	// * @param newsId
	// * @return
	// */
	// @Query("select count(*) from NewsCommentInfo where newsId = ?1 and enabled = 1")
	// Integer commentCount(String newsId);
	//
	// /**
	// * 资讯评论列表
	// * @param newsId
	// * @param pageable
	// * @return
	// */
	// @Query("select id,createdTime,userId,newsId,content from NewsCommentInfo where createdTime <
	// ?1 and enabled = 1 order by createdTime desc")
	// List<NewsComment> findByNewsId(String newsId, Pageable pageable);
}
