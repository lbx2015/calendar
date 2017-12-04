package net.riking.dao.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QACAgreeRel;

/**
 * 
 * 〈问题回答评论点赞信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface QACAgreeRelRepo extends JpaRepository<QACAgreeRel, String>, JpaSpecificationExecutor<QACAgreeRel> {
	/**
	 * 统计回答评论点赞数
	 * @param newsCommentId
	 * @return
	 */
	@Query("select count(*) from QACAgreeRel where qacId = ?1 and dataType = ?2")
	Integer agreeCount(String qacId, Integer dataType);

	/**
	 * 问题回答取消点赞
	 * @param userId
	 * @param qacId
	 * @param dataType
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("delete QACAgreeRel where userId =?1 and qacId = ?2 and dataType = ?3")
	void deleteByUIdAndQaId(String userId, String qaId, Integer dataType);
}
