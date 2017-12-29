package net.riking.dao.repo;

import java.util.List;

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
	
	@Query(" from SysDays where dates = ?1 ")
	SysDays findByDates(String dates);
	
	/***
	 * 获取已维护好的国家节假日年份
	 * @author james.you
	 * @version crateTime：2017年12月22日 下午8:54:11
	 * @used TODO
	 * @return
	 */
	@Query("select DISTINCT SUBSTRING(dates, 1, 4) from SysDays where enabled=1 ")
	List<String> findEnabledOnlyYear();
}
