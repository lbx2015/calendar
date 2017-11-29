package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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

}
