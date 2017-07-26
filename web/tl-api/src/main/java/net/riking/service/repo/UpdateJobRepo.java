package net.riking.service.repo;

import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.core.entity.model.Job;
@Repository
public interface UpdateJobRepo extends JpaRepository<Job, String>  {
	@Transactional
	@Modifying
	@Query("update Job set startState='PRE_RECROD' where jobId in ?1")
	int updateStartState(Set<String> ids);

}
