package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Days;

@Repository
public interface DaysRepo extends JpaRepository<Days, String>, JpaSpecificationExecutor<Days>{
 	@Query("select count(*) from Days where date BETWEEN ?1 and ?2 and isWork = '1' ")
	Integer findByDate(String date1 ,String date2);
 	
 	@Query("select count(*) from Days where date BETWEEN ?1 and ?2  ")
 	Integer findByAllDate(String date1 ,String date2);
}
