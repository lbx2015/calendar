package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.QAInvite;

/**
 * 用户邀请回答表
 * @author jc.tan 2017年12月2日
 * @see
 * @since 1.0
 */
@Repository
public interface QAInviteRepo extends JpaRepository<QAInvite, String>, JpaSpecificationExecutor<QAInvite> {
	/**
	 * 根据联合主键查出唯一数据
	 * @param userId
	 * @param toUserId
	 * @param questionId
	 * @return
	 */
	@Query("from QAInvite where userId = ?1 and toUserId = ?2 and questionId = ?3")
	QAInvite findByOne(String userId, String toUserId, String questionId);

	/**
	 * 
	 * @param userId
	 * @param questionId
	 * @return
	 */
	@Query("select toUserId from QAInvite where userId = ?1 and questionId = ?2")
	List<String> findToIdByUIdAndQId(String userId, String questionId);
}
