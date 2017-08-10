package net.riking.service.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import net.riking.entity.model.Remind;

/**
 * 
 * @author lucky.liu
 * @version crateTime：2017年8月8日 下午4:07:56
 * @used TODO
 */

@Repository
public interface RemindRepo extends JpaRepository<Remind, String>, JpaSpecificationExecutor<Remind> {

	List<Remind> findByUserId(String userId);

	@Query(" from Remind  where userId=?1 and (((repeatFlag=3 and repeatValue like %?3% ) or repeat_flag=?4) or (repeatFlag=0 and strDate=?2)) ")
	List<Remind> findOneDay(String userId, String date, String currWeek, Integer repeatFlag);

	@Query("select r.strDate from Remind r where r.userId=?1 and r.repeatFlag=0 and substring(r.strDate, 0, 6) =?2")
	Set<String> findMouthByStrDate(String userId, String date);

	@Query("select count(*) from Remind  where userId=?1 and repeatFlag=?2")
	Long findMouthByRepeatFlagForWork(String userId, Integer repeatFlag);

	@Query("select count(*) from Remind  where userId=?1 and repeatFlag=3 and repeatValue like %?2%")
	Long findMouthByWeek(String userId, String currWeek);

}
