package net.riking.dao.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUser;
import net.riking.entity.model.AppUserDetail;

@Repository
public interface AppUserDetailRepo extends JpaRepository<AppUserDetail, String>, JpaSpecificationExecutor<AppUserDetail> {


//	@Query(" from AppUser where is_deleted = 1 and phone = ?1 ")
//	AppUser findByOne(String id);
	
	 @Query("select a.phoneDeviceid from AppUserDetail a where a.isDeleted = '1' and substring(a.birthday, 5, 4) =?1 ")
	 Set<String> findByDate(String date);

}
