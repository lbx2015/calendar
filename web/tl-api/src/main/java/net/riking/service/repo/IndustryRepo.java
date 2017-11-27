package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import net.riking.entity.model.Industry;

public interface IndustryRepo extends JpaRepository<Industry, String>, JpaSpecificationExecutor<Industry> {

	// @Query(" from Industry u where u.type = ?1")
	// List<Industry> findIndustry(Integer type);
	//
	// @Query(" from Industry u where u.parentId = ?1")
	// List<Industry> findPositionByIndustry(Long id);

}
