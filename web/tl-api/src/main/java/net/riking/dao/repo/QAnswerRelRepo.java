package net.riking.dao.repo;

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

}
