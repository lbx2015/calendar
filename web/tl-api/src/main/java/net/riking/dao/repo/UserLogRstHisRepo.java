package net.riking.dao.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.UserLogRstHis;

@Repository
public interface UserLogRstHisRepo
		extends JpaRepository<UserLogRstHis, String>, JpaSpecificationExecutor<UserLogRstHis> {

	/** ------------------------- web --------------------- **/

	/**
	 * @param begin
	 * @param end
	 * @param dataType
	 * @return
	 */
	@Query("select count(*) from UserLogRstHis where createdTime between ?1 and ?2 and dataType = ?3")
	Integer countByTimeAndType(Date begin, Date end, Integer dataType);
}
