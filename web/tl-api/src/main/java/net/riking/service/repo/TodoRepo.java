package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import net.riking.entity.model.Todo;

@Repository
public interface TodoRepo extends JpaRepository<Todo, String>, JpaSpecificationExecutor<Todo> {

	/**
	 * @author Lucky.Liu on 2017/8/05.
	 */
	List<Todo> findByUserId(String userId);

	@Modifying
	@Transactional
	@Query("delete from Todo  where todoId in ?1")
	int deleteById(Set<String> ids);
}
