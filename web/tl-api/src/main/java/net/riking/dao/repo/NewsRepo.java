package net.riking.dao.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
public interface NewsRepo extends JpaRepository<News, String>, JpaSpecificationExecutor<News> {

	/**
	 * 根据时间戳返回资讯列表（下一页数据）
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select new net.riking.entity.model.News(n.id,n.createdTime,n.title,n.seat,n.coverUrls,n.content,n.issued,(select a.userName from AppUser a where n.createdBy = a.id and a.isDeleted=1) as userName) from News n where n.createdTime < ?1 and n.isDeleted=1 and n.isAduit <> 2 order by n.createdTime desc")
	List<News> findNewsListPageNext(Date reqTimeStamp, Pageable pageable);

	/**
	 * 根据时间戳返回资讯列表（刷新第一页数据顺序排列）
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select new net.riking.entity.model.News(n.id,n.createdTime,n.title,n.seat,n.coverUrls,n.content,n.issued,(select a.userName from AppUser a where n.createdBy = a.id and a.isDeleted=1) as userName) from News n where n.createdTime > ?1 and n.isDeleted=1 and n.isAduit <> 2 order by n.createdTime asc")
	List<News> findNewsListRefresh(Date reqTimeStamp, Pageable pageable);

	/**
	 * 资讯的详情
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select new net.riking.entity.model.News(n.id,n.createdTime,n.title,n.seat,n.coverUrls,n.content,n.issued) from News n where n.id =?1")
	News getById(String newsId);

}