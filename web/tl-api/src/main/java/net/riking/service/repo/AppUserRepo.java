package net.riking.service.repo;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUser;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser>{
	@Transactional
	@Modifying
	@Query("delete from AppUser u where u.id in ?1")
	public int deleteByIds(Set<String> ids);
}
