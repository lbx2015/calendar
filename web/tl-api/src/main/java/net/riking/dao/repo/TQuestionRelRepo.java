package net.riking.dao.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.TQuestionRel;

/**
 * 
 * 〈话题下面的问题关注或屏蔽信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface TQuestionRelRepo extends JpaRepository<TQuestionRel, String>, JpaSpecificationExecutor<TQuestionRel> {

	/**
	 * 问题关注数
	 * @param newsId
	 * @return
	 */
	@Query("select count(*) from TQuestionRel where tqId = ?1 and dataType = ?2")
	Integer followCount(String tqId, Integer dataType);

	/**
	 * 话题下面的问题取消关注或屏蔽
	 * @param userId
	 * @param newsId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("delete TQuestionRel where userId =?1 and tqId = ?2 and dataType =?3")
	void deleteByUIdAndTqId(String userId, String tqId, Integer dataType);

}
