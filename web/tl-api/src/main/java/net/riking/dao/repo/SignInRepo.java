package net.riking.dao.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.SignIn;

/**
 * 
 * 〈每日签到表〉
 * 
 * @author jc.tan 2017年11月23日
 * @see
 * @since 1.0
 */
@Repository
public interface SignInRepo extends JpaRepository<SignIn, String>, JpaSpecificationExecutor<SignIn> {
	@Query("from SignIn where userId = ?1 and  createdTime between ?2 and ?3")
	SignIn getByUIdAndTime(String userId, Date begin, Date end);
}
