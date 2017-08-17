package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUser;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, String>, JpaSpecificationExecutor<AppUser>{
	@Transactional
	@Modifying
	@Query("update AppUser set deleteState = '0'  where id in ?1")
	int deleteByIds(Set<String> ids);
	
	@Transactional
	@Modifying
	@Query("update AppUser set enabled = '1'  where id = ?1")
	int enable(String id);
	
	@Transactional
	@Modifying
	@Query("update AppUser set enabled = '0'  where id = ?1")
	int unEnable(String id);
	
	@Transactional
	@Modifying
	@Query("update AppUser set photoUrl = ?2  where id = ?1")
	int updatePhoto(String id,String photo);
	
	@Transactional
	@Modifying
	@Query("update AppUser set passWord = '123456'  where id = ?1")
	int passwordReset(String id);
	
	@Query("select a.phoneSeqNum from AppUser a where a.deleteState = '1' and substring(a.birthday, 5, 4) =?1 ")
	Set<String> findByDate(String date);
	

	@Query(" from AppUser  where deleteState = '1' and id = ?1 ")
	AppUser findById(String id);

	List<AppUser> findByDeleteStateAndTelephone(String deleteState,String telephone);

}
