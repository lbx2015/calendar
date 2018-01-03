package net.riking.dao.repo;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUserDetail;

@Repository
public interface AppUserDetailRepo
		extends JpaRepository<AppUserDetail, String>, JpaSpecificationExecutor<AppUserDetail> {

	// @Query(" from AppUser where is_deleted = 1 and phone = ?1 ")
	// AppUser findByOne(String id);

	@Query("select integral from AppUserDetail where id = ?1 ")
	Integer getIntegral(String userId);
	
	@Query("select new net.riking.entity.model.AppUserDetail(u.id, u.userName, u.photoUrl) from AppUserDetail u where u.id in ?1 ")
	List<AppUserDetail> findAllByIds(Collection<String> ids);

	@Transactional
	@Modifying
	@Query("update AppUserDetail set integral = ?1 where id=?2")
	int updIntegral(Integer integral, String userId);

	@Transactional
	@Modifying
	@Query("update AppUserDetail set photoUrl = ?2 where id = ?1")
	int updatePhoto(String userId, String photo);
}
