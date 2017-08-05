package net.riking.service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.RemindHis;

@Repository
public interface RemindHisRepo extends JpaRepository<RemindHis, String>, JpaSpecificationExecutor<RemindHis> {
	List<RemindHis> findByUserId(String userId);
}