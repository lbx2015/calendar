package net.riking.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Todo;

@Repository
public interface TodoRepo extends JpaRepository<Todo, String>, JpaSpecificationExecutor<Todo> {
	List<Todo> findByUserId(String userId);
}
