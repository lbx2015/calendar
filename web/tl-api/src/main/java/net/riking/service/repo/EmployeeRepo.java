 package net.riking.service.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Employee;

import java.util.Set;

 @Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {

	Employee findById(Long id);

 	@Transactional
	@Modifying
	@Query("delete from Employee e where e.id in ?1")
	int deleteByIds(Set<Long> ids);
	
//	@Transactional
//	@Modifying
//	@Query("update Employee e set e.curJobState = ?2 where e.jobId = ?1")
//	int updateStateByJobId(String jobId,String state);
}
