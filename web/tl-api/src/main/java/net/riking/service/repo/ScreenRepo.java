package net.riking.service.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Screen;

@Repository
public interface ScreenRepo extends JpaRepository<Screen, Long>, JpaSpecificationExecutor<Screen> {

	@Query("select t.questionId from Screen t where t.userId = ?")
	Set<String> findByUserId(String userId);
}
