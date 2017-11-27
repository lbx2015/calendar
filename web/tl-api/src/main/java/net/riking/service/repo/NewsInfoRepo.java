package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.News;

/**
 * 
 * 〈行业资讯表〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NewsInfoRepo extends JpaRepository<News, String>, JpaSpecificationExecutor<News> {
	//
	// /**
	// * 根据时间戳返回资讯列表（下一页数据）
	// * @param reqTimeStamp
	// * @return
	// */
	// @Query("select n.id,n.createdTime,n.title,n.author from NewsInfo n where n.createdTime < ?1
	// and n.enabled = 1 order by n.createdTime desc")
	// List<News> findNewsListPageNext(String reqTimeStamp, Pageable pageable);

	// /**
	// * 根据时间戳返回资讯列表（刷新第一页数据顺序排列）
	// * @param reqTimeStamp
	// * @return
	// */
	// @Query("select n.id,n.createdTime,n.title,n.author from NewsInfo n where n.createdTime > ?1
	// and n.enabled = 1 order by n.createdTime asc")
	// List<News> findNewsListRefresh(String reqTimeStamp, Pageable pageable);
	//
	// /**
	// * 资讯的详情
	// * @param reqTimeStamp
	// * @return
	// */
	// @Query("select n.id,n.createdTime,n.title,n.author,n.content from NewsInfo n where n.id =
	// ?1")
	// News getById(String newsId);

}
