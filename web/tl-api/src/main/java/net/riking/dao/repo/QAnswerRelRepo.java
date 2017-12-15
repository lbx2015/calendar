package net.riking.dao.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QAnswerRel;

/**
 * 
 * 〈问题回答收藏点赞〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface QAnswerRelRepo extends JpaRepository<QAnswerRel, String>, JpaSpecificationExecutor<QAnswerRel> {
	/**
	 * 问题回答取消收藏点赞
	 * @param userId
	 * @param newsId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("delete QAnswerRel where userId =?1 and qaId = ?2 and dataType = ?3")
	void deleteByUIdAndQaId(String userId, String qaId, Integer dataType);

	/**
	 * 查询点赞/收藏的qaId
	 * @param questAnswerId
	 * @return
	 */
	@Query("select qaId from QAnswerRel where userId = ?1 and dataType = ?2")
	List<String> findByUser(String userId, Integer dataType);

	/**
	 * 查询点赞/收藏的qaId
	 * @param questAnswerId
	 * @return
	 */
	@Query("from QAnswerRel where userId = ?1 ")
	List<QAnswerRel> findByUser(String userId);

	/**
	 * 根据userId,qaId找出唯一记录
	 * @param userId,qaId
	 * @return
	 */
	@Query(" from QAnswerRel where userId = ?1 and qaId = ?2 and dataType = ?3")
	QAnswerRel findByOne(String userId, String qaId, Integer dataType);

	/**
	 * 统计回答评论点赞数
	 * @param newsCommentId
	 * @return
	 */
	@Query("select count(*) from QAnswerRel where qaId = ?1 and dataType = ?2")
	Integer agreeCount(String qaId, Integer dataType);
}
