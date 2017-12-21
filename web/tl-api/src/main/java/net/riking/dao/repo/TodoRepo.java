package net.riking.dao.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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

	/**
	 * 获取代办
	 * @param userId
	 * @param pageable
	 * @return
	 */
	@Query("from Todo where userId =?1 and isComplete = ?2 order by isImportant desc,strDate desc")
	List<Todo> findTodo(String userId, Integer isComplete, Pageable pageable);

	// List<Todo> findByUserId(String userId);
	//
	// @Modifying
	// @Transactional
	// @Query("delete from Todo where todoId in ?1")
	// int deleteById(Set<String> ids);
}
