package net.riking.dao.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUser;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, String>, JpaSpecificationExecutor<AppUser> {

	@Query(" from AppUser where isDeleted = 1 and phone = ?1 ")
	AppUser findByPhone(String phone);

	@Query(" from AppUser where isDeleted = 1 and openId = ?1 ")
	AppUser findByOpenId(String openId);

	// @Query("select new
	// net.riking.entity.resp.AppUserResp(a.id,a.userName,a.openId,a.email,a.phone,ap.realName,ap.companyName,ap.sex,ap.birthday,ap.address,ap.description,ap.phoneDeviceid,ap.integral,ap.experience,ap.photoUrl,ap.remindTime,ap.isSubscribe,ap.industryId,ap.positionId,ap.isGuide)
	// from AppUser a join a.AppUserDetail ap where a.isDeleted = 1 and a.id = ?1 and ap.id = a.id")
	// AppUserResp getById(String userId);

	// TODO 暂时注释 @Transactional
	// @Modifying
	// @Query("update AppUser set deleteState = '0' where id in ?1")
	// int deleteByIds(Set<String> ids);

	// @Transactional
	// @Modifying
	// TODO 暂时注释
	// @Query("update AppUser set enabled = '1' where id = ?1")
	// int enable(String id);

	// @Transactional
	// @Modifying
	// TODO 暂时注释
	// @Query("update AppUser set enabled = '0' where id = ?1")
	// int unEnable(String id);

	@Transactional
	@Modifying
	@Query("update AppUserDetail set photoUrl = ?2 where id = ?1")
	int updatePhoto(String userId, String photo);

	// @Transactional
	// @Modifying
	// TODO 暂时注释
	// @Query("update AppUser set passWord = '123456' where id = ?1")
	// int passwordReset(String id);

	// TODO 暂时注释
	// @Query("select a.phoneSeqNum from AppUser a where a.deleteState = '1' and
	// substring(a.birthday, 5, 4) =?1 ")
	// Set<String> findByDate(String date);

	// TODO 暂时注释
	// @Query("select a.phoneSeqNum from AppUser a where a.deleteState = '1' and
	// a.id in ?1 ")
	// Set<String> findByUserId(Set<String> userId);
	// TODO 暂时注释
	// List<AppUser> findByDeleteStateAndTelephone(String deleteState, String
	// telephone);

	// TODO 暂时注释
	// @Query(" from AppUser where deleteState = '1' and id = ?1 ")
	// AppUser findById(String id);

	// TODO 暫時注釋
	// @Query(" from AppUser where openId = ?1 ")
	// AppUser findByOpenId(String openId);

	/**
	 * 根据userId找出已关注的人
	 * 
	 * @param userId
	 * @return
	 */
	// TODO 暫時注釋
	// @Query("select a.id,a.name from AppUser a join UserFollowInfo u where
	// u.userId = ?1 and
	// u.toUserId = a.id and u.enabled = 1")
	// List<AppUser> findFollowInfoByUserId(String userId);
}
