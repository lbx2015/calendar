package net.riking.service.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QuestionIgnore;

/**
 * 
 * 〈问题屏蔽表〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface QuestionIgnoreRepo
		extends JpaRepository<QuestionIgnore, String>, JpaSpecificationExecutor<QuestionIgnore> {

	/**
	 * 问题屏蔽或取消屏蔽
	 * @param userId
	 * @param newsId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("update QuestionIgnore set enabled = 0  where userId =?1 and questionId = ?2")
	QuestionIgnore updInvalid(String userId, String questionId);

}
