package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.AppUserGrade;

@Repository
public interface AppUserGradeRepo extends JpaRepository<AppUserGrade, Integer>, JpaSpecificationExecutor<AppUserGrade> {

	@Query("from AppUserGrade ")
	List<AppUserGrade> findByIsDeleted(Integer isDeleted);

}
