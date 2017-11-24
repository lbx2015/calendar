package net.riking.service.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.NewsInfo;

/**
 * 
 * 〈行业资讯表〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NewsInfoRepo extends JpaRepository<NewsInfo, String>, JpaSpecificationExecutor<NewsInfo> {

	@Query("select count(*) from Days where date BETWEEN ?1 and ?2  ")
	Integer commentCount(String date1, String date2);

	/**
	 * 根据时间戳返回资讯列表（下一页数据）
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select n.id,n.createdTime,n.title,n.author from NewsInfo n where createdTime < ?1 and enabled = 1 order by createdTime desc")
	List<NewsInfo> findNewsListPageNext(String reqTimeStamp, Pageable pageable);

	/**
	 * 根据时间戳返回资讯列表（刷新第一页数据顺序排列）
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select n.id,n.createdTime,n.title,n.author from NewsInfo n where createdTime > ?1 and enabled = 1 order by createdTime asc")
	List<NewsInfo> findNewsListRefresh(String reqTimeStamp, Pageable pageable);

	/**
	 * 资讯的详情
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select n.id,n.createdTime,n.title,n.author,n.content from NewsInfo n where id = ?1")
	NewsInfo getById(String newsId);

}
