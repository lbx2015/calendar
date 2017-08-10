package net.riking.service.repo;

import net.riking.entity.model.BusinessDay;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月9日 上午10:33:07
 * @used TODO
 */
@Repository
public interface BusinessDayRepo extends JpaRepository<BusinessDay, Long> {
	
	@Query("select b.businessDay from BusinessDay b where b.businessDay like %?1%")
	Set<String> finbByMonth(String date);
}
