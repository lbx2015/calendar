package net.riking.dao.repo;

import java.util.Collection;
import java.util.List;

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
public interface AppUserFollowRelRepo
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
	 * 根据主键找到唯一一条关注记录
	 * @param userId
	 * @param toUserId
	 * @return
	 */
	@Query(" from UserFollowRel where userId = ?1 and toUserId = ?2")
	UserFollowRel getByUIdAndToId(String userId, String toUserId);

	
	@Query(" from UserFollowRel where (userId = ?1 and toUserId = ?2) or  (userId = ?2 and toUserId = ?1)")
	UserFollowRel getByUIdOrToId(String userId, String toUserId);
	/**
	 * 查询用户关注的id
	 * @param newsId
	 * @return
	 */
	@Query(" from UserFollowRel where userId = ?1 ")
	List<UserFollowRel> findByUser(String userId);

	/**
	 * 查询用户粉丝数
	 * @param newsId
	 * @return
	 */
	@Query("select count(*) from UserFollowRel where toUserId = ?1 ")
	Integer countByToUser(String userId);

	/**
	 * 查询用户粉丝数
	 * @param newsId
	 * @return
	 */
	@Query("select count(*) from UserFollowRel where userId = ?1 ")
	Integer countByUser(String userId);
	
	
	@Query(" from UserFollowRel where ( userId = ?1 and followStatus != 0 ) or ( toUserId = ?1 and followStatus= 2 ) ")
	List<UserFollowRel> findByUserId(String userId);
	
	
	@Query("from UserFollowRel where (userId in ?1 and followStatus != 0) or (toUserId in ?1 and followStatus= 2) ")
	List<UserFollowRel> findUserIdByUserIds(Collection<String> userId);
}
