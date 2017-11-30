package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUserDetail;

@Repository
public interface AppUserDetailRepo
		extends JpaRepository<AppUserDetail, String>, JpaSpecificationExecutor<AppUserDetail> {

	// @Query(" from AppUser where is_deleted = 1 and phone = ?1 ")
	// AppUser findByOne(String id);

}
