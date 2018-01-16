package net.riking.dao.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserGrade;
import net.riking.entity.model.AppUserResult;
import net.riking.entity.model.EmailSuffix;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, String>, JpaSpecificationExecutor<AppUser> {

	@Query(" from AppUser where isDeleted = 1 and phone = ?1 ")
	AppUser findByPhone(String phone);
	
	@Query("from AppUser where isDeleted = 1 and phone in ?1 ")
	List<AppUser> findByPhones(List<String> phones);

	@Query(" from AppUser where isDeleted = 1 and openId = ?1 ")
	AppUser findByOpenId(String openId);

	@Query(" from AppUserGrade")
	List<AppUserGrade> findGrade();

	@Query(" from EmailSuffix where enabled = 1")
	List<EmailSuffix> findEmailSuffix();

	/**
	 * 根据关键字模糊查询userName
	 * 
	 * @param keyWord
	 * @return
	 */
	@Query("select new net.riking.entity.model.AppUserResult(a.id,a.userName,(select app.photoUrl from AppUserDetail app where a.id = app.id),(select app.experience from AppUserDetail app where a.id = app.id),(select userId from UserFollowRel where userId = ?2 and toUserId = a.id)) from AppUser a where a.isDeleted = 1 and a.userName like %?1% ")
	List<AppUserResult> getUserByParam(String keyWord, String userId);

	/**
	 * 根据email模糊查询
	 * 
	 * @param keyWord
	 * @return
	 */
	@Query("select new net.riking.entity.model.AppUserResult(a.id,a.userName,(select app.photoUrl from AppUserDetail app where a.id = app.id),(select app.experience from AppUserDetail app where a.id = app.id),(select userId from UserFollowRel where userId = ?2 and toUserId = a.id)) from AppUser a where a.id <>?2 and a.isDeleted = 1 and a.email like %?1 ")
	List<AppUserResult> findAllByEmail(String email, String userId, Pageable pageable);

	@Transactional
	@Modifying
	@Query("update AppUser set isIdentified = 1,email = ?2 where id = ?1")
	void updEmailIndentify(String userId, String email);

	// @Query("select new
	// net.riking.entity.resp.AppUserResp(a.id,a.userName,a.openId,a.email,a.phone,ap.realName,ap.companyName,ap.sex,ap.birthday,ap.address,ap.description,ap.phoneDeviceid,ap.integral,ap.experience,ap.photoUrl,ap.remindTime,ap.isSubscribe,ap.industryId,ap.positionId,ap.isGuide)
	// from AppUser a join a.AppUserDetail ap where a.isDeleted = 1 and a.id =
	// ?1 and ap.id = a.id")
	// AppUserResp getById(String userId);

	/******** WEB ************/
	@Transactional
	@Modifying
	@Query("update AppUser set isDeleted =0 where id in ?1")
	int deleteByIds(Set<String> ids);

	@Transactional
	@Modifying
	@Query("update AppUser set enabled = '1' where id = ?1")
	int enable(String id);

	@Transactional
	@Modifying
	@Query("update AppUser set enabled = '0' where id = ?1")
	int unEnable(String id);

	@Query("select id from AppUser where isDeleted='1' and userName=?1")
	Set<String> getUserIdsByUserName(String userName);

	/******** WEB END ************/

	// @Transactional
	// @Modifying
	// TODO 暂时注释
	// @Query("update AppUser set passWord = '123456' where id = ?1")
	// int passwordReset(String id);

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
