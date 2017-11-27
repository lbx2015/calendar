package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.NewsRel;

/**
 * 
 * 〈行业资讯收藏〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NewsCollectInfoRepo extends JpaRepository<NewsRel, String>, JpaSpecificationExecutor<NewsRel> {
	// /**
	// * 资讯收藏
	// * @param userId
	// * @param newsId
	// * @return
	// */
	// @Transactional
	// @Modifying
	// @Query("update NewsCollectInfo set enabled = 0 where userId =?1 and newsId = ?2")
	// NewsComment updInvalid(String userId, String newsId);

}
