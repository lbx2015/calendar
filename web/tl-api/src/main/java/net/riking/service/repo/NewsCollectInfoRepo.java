package net.riking.service.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.NewsCollectInfo;
import net.riking.entity.model.NewsCommentInfo;

/**
 * 
 * 〈行业资讯收藏〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NewsCollectInfoRepo
		extends JpaRepository<NewsCollectInfo, String>, JpaSpecificationExecutor<NewsCollectInfo> {
	@Transactional
	@Modifying
	@Query("update NewsCollectInfo set enabled = 0  where userId =?1 and newsId = ?2")
	NewsCommentInfo updInvalid(String userId, String newsId);

}
