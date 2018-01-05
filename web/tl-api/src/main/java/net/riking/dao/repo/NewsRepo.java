package net.riking.dao.repo;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.News;
import net.riking.entity.model.NewsResult;

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
	 * 
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select new net.riking.entity.model.News(n.id,n.createdTime,n.modifiedTime,n.title,n.seat,n.coverUrls,n.content,n.issued,(select a.userName from AppUser a where n.createdBy = a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where n.createdBy = ap.id),(select app.experience from AppUserDetail app where n.createdBy = app.id)) from News n where n.createdTime < ?1 and n.isDeleted=1 and n.isAduit <> 2 order by n.createdTime desc")
	List<News> findNewsListPageNext(Date reqTimeStamp, Pageable pageable);

	/**
	 * 根据时间戳返回资讯列表（刷新第一页数据顺序排列）
	 * 
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select new net.riking.entity.model.News(n.id,n.createdTime,n.modifiedTime,n.title,n.seat,n.coverUrls,n.content,n.issued,(select a.userName from AppUser a where n.createdBy = a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where n.createdBy = ap.id),(select app.experience from AppUserDetail app where n.createdBy = app.id)) from News n where n.createdTime > ?1 and n.isDeleted=1 and n.isAduit <> 2 order by n.createdTime asc")
	List<News> findNewsListRefresh(Date reqTimeStamp, Pageable pageable);

	/**
	 * 资讯的详情
	 * 
	 * @param reqTimeStamp
	 * @return
	 */
	@Query("select new net.riking.entity.model.News(n.id,n.createdTime,n.modifiedTime,n.title,n.seat,n.coverUrls,n.content,n.issued) from News n where n.id =?1")
	News getById(String newsId);

	/**
	 * 根据关键字模糊查询title
	 * 
	 * @param keyWord
	 * @return
	 */
	@Query("select new net.riking.entity.model.NewsResult(n.id,n.title,n.createdTime) from News n where n.isDeleted = 1 and n.isAduit <> 2 and n.title like %?1% ")
	List<NewsResult> getNewsByParam(String keyWord);

	/*********** WEB ************/
	@Transactional
	@Modifying
	@Query(" update News set isDeleted=0 where id in ?1 ")
	int deleteById(Set<String> ids);

	@Transactional
	@Modifying
	@Query(" update News set isAduit=1 where id in ?1 ")
	int verifyById(Set<String> ids);

	@Transactional
	@Modifying
	@Query(" update News set isAduit=2 where id in ?1 ")
	int verifyNotPassById(Set<String> ids);

	@Query("select id from News where title=?1")
	Set<String> getNewsIdsByTitle(String title);
}
