package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Todo;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:33:48
 * @used TODO
 */
@Repository
public interface TodoRepo extends JpaRepository<Todo, String>, JpaSpecificationExecutor<Todo> {

	// List<Todo> findByUserId(String userId);
	//
	// @Modifying
	// @Transactional
	// @Query("delete from Todo where todoId in ?1")
	// int deleteById(Set<String> ids);
}
