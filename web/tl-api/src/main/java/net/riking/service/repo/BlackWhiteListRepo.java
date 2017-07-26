package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.riking.entity.model.BlackWhiteList;

@Repository
public interface BlackWhiteListRepo extends JpaRepository<BlackWhiteList, Long>, JpaSpecificationExecutor<BlackWhiteList> {

	@Transactional
	@Modifying
	@Query("delete from BlackWhiteList bwl where bwl.id in (?1)")
	Integer deleteByIds(Set<Long> ids);

	@Query("from BlackWhiteList b where (b.ishs<>2 or b.ishs is null) and b.hmdlx=1")
	Set<BlackWhiteList> getBlackWhiteLists();

	@Transactional
	@Modifying
	@Query("update BlackWhiteList b set b.ishs=2 where (b.ishs<>2 or b.ishs is null) and b.hmdlx=1")
	Integer setIshs();
	
	List<BlackWhiteList> findByMdly(String mdly);
}
