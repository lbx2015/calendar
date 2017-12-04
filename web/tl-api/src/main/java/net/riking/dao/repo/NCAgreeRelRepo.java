package net.riking.dao.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.NCAgreeRel;

/**
 * 
 * 〈资讯评论点赞信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface NCAgreeRelRepo extends JpaRepository<NCAgreeRel, String>, JpaSpecificationExecutor<NCAgreeRel> {
	/**
	 * 统计资讯评论点赞数
	 * @param ncId
	 * @return
	 */
	@Query("select count(*) from NCAgreeRel where ncId = ?1 and dataType = ?2")
	Integer agreeCount(String ncId, Integer dataType);

	/**
	 * 资讯评论取消点赞
	 * @param userId
	 * @param qacId
	 * @param dataType
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("delete NCAgreeRel where userId =?1 and ncId = ?2 and dataType = ?3")
	void deleteByUIdAndNcId(String userId, String ncId, Integer dataType);
}
