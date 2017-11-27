package net.riking.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QAnswerAgreeInfo;

/**
 * 
 * 〈问题回答点赞信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface QAnswerAgreeInfoRepo
		extends JpaRepository<QAnswerAgreeInfo, String>, JpaSpecificationExecutor<QAnswerAgreeInfo> {
	/**
	 * 根据操作人主键（userId）找出点赞的问题回答Id
	 * @param userId
	 * @return
	 */
	@Query("select questionAnswerId from QAnswerAgreeInfo where userId =?1 and enabled =1")
	List<String> findByUserId(String userId);

}
