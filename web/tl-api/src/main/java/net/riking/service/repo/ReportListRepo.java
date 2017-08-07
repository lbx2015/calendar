package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.ReportList;
@Repository
public interface ReportListRepo extends JpaRepository<ReportList, String>, JpaSpecificationExecutor<ReportList> {
	
	/**
	 * @author Lucky.Liu on 2017/8/05.
	 */

	@Query(" from ReportList  where Id in ?1 and deleteState = '1'  ")
	List<ReportList> findbyReoprtId(Set<String> reoprtId);
	
	@Transactional
	@Modifying
	@Query(" update ReportList set deleteState = '0' where id in ?1  ")
	int deleteById(Set<String> ids);
}
