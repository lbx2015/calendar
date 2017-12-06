package net.riking.dao.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
public interface NewsRelRepo extends JpaRepository<NewsRel, String>, JpaSpecificationExecutor<NewsRel> {
	/**
	 * 资讯取消收藏
	 * @param userId
	 * @param newsId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("delete NewsRel where userId =?1 and newsId = ?2 and dataType = ?3")
	void deleteByUIdAndNId(String userId, String newsId, Integer dataType);

	/**
	 * 查询收藏的资讯id
	 * @param ncId
	 * @return
	 */
	@Query("select newsId from NewsRel where userId = ?1 and dataType = ?2")
	List<String> findByUserId(String userId, Integer dataType);

	/**
	 * 根据userId,newsId找出唯一记录
	 * @param userId,newsId
	 * @return
	 */
	@Query(" from NewsRel where userId = ?1 and newsId = ?2 and dataType = ?3")
	NewsRel findByOne(String userId, String newsId, Integer dataType);
}
