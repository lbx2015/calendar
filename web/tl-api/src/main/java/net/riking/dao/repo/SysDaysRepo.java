package net.riking.dao.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.SysDays;

@Repository
public interface SysDaysRepo extends JpaRepository<SysDays, String>, JpaSpecificationExecutor<SysDays> {
	@Query("select count(*) from SysDays where dates BETWEEN ?1 and ?2 and isWork = '1' ")
	Integer findByDate(String date1, String date2);

	@Query("select count(*) from SysDays where dates BETWEEN ?1 and ?2 ")
	Integer findByAllDate(String date1, String date2);
}
