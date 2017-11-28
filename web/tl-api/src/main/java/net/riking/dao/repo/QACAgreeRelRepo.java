package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
}
