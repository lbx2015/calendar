package net.riking.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Remind;

@Repository
public interface RemindRepo extends JpaRepository<Remind, String>, JpaSpecificationExecutor<Remind> {

	List<Remind> findByUserId(String userId);
}
