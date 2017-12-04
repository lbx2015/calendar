package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import net.riking.entity.model.Industry;

public interface IndustryRepo extends JpaRepository<Industry, String>, JpaSpecificationExecutor<Industry> {

	@Query(" from Industry u where u.dataType = ?1")
	List<Industry> findIndustry(Integer type);

	@Query(" from Industry u where u.parentId = ?1")
	List<Industry> findPositionByIndustry(String id);

}
