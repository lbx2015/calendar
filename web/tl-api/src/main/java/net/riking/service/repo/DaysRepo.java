package net.riking.service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Days;

@Repository
public interface DaysRepo extends JpaRepository<Days, String>, JpaSpecificationExecutor<Days>{
 	
}
