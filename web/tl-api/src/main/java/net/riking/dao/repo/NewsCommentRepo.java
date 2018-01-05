package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
@Repository("newsCommentRepo")
public interface NewsCommentRepo extends JpaRepository<NewsComment, String>, JpaSpecificationExecutor<NewsComment> {
	/**
	 * 统计资讯评论数
	 * @param newsId
	 * @return
	 */
	@Query("select count(*) from NewsComment where newsId = ?1 and isAduit <> 2 and isDeleted = 1")
	Integer commentCount(String newsId);

	/**
	 * 统计资讯评论数
	 * @param newsId
	 * @return
	 */
	@Query("select count(*) from NewsComment where newsId = ?1 and isAduit = ?2 and isDeleted = 1")
	Integer commentCountByNewsIdAndIsAduit(String newsId, Integer isAduit);

	/**
	 * 资讯评论列表
	 * @param newsId
	 * @param pageable
	 * @return
	 */
	@Query("select new net.riking.entity.model.NewsComment(n.id,n.createdTime,n.modifiedTime,n.userId,n.newsId,n.content,(select a.userName from AppUser a where n.userId= a.id and a.isDeleted=1),(select ap.photoUrl from AppUserDetail ap where n.userId = ap.id),(select app.experience from AppUserDetail app where n.userId = app.id),(select ncId from NCAgreeRel where userId = ?2 and ncId = n.id and dataType = 1)) from NewsComment n where n.newsId= ?1 and n.isAduit <> 2 and n.isDeleted=1 order by n.createdTime desc")
	List<NewsComment> findByNewsId(String newsId, String userId, Pageable pageable);
}
