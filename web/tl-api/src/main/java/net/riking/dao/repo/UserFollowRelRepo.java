package net.riking.dao.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.UserFollowRel;

/**
 * 
 * 〈用户关注信息〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface UserFollowRelRepo
		extends JpaRepository<UserFollowRel, String>, JpaSpecificationExecutor<UserFollowRel> {
	/**
	 * 用户取消关注
	 * @param userId
	 * @param newsId
	 * @return
	 */
	@Transactional
	@Modifying
	@Query("delete UserFollowRel where userId =?1 and toUserId = ?2")
	void deleteByUIdAndToId(String userId, String toUserId);

	/**
	 * 根据主键修改用户关注状态
	 * @param userId
	 * @param toUserId
	 * @param followStatus
	 * @return
	 */
	@Transactional
	@Modifying
	@Query(" update UserFollowRel set followStatus = ?3 where userId = ?1 and toUserId = ?2")
	void updFollowStatus(String userId, String toUserId, Integer followStatus);

	/**
	 * 根据主键找到唯一一条点赞记录
	 * @param userId
	 * @param toUserId
	 * @return
	 */
	@Query(" from UserFollowRel where userId = ?1 and toUserId = ?2")
	UserFollowRel getByUIdAndToId(String userId, String toUserId);

}
