package net.riking.dao.repo;

import java.util.List;

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

	// @Transactional
	// @Modifying
	// @Query("update ReportSubscribeRel set isCompleted=1 and completedDate=?5 where userId = ?1
	// and reportId = ?2 and submitStartTime = ?3 and submitEndTime = ?4 ")
	// public int updateComplated(String userId, String reportId, String submitStartTime, String
	// submitEndTime, String completedDate);

	// List<Remind> findByUserId(String userId);
	//
	// @Query(" from Remind where userId=?1 and (((repeatFlag=3 and repeatValue like %?3% ) or
	// repeat_flag=?4) or (repeatFlag=0 and strDate=?2)) ")
	// List<Remind> findOneDay(String userId, String date, String currWeek, Integer repeatFlag);
	//
	// @Query("select r.strDate from Remind r where r.userId=?1 and r.repeatFlag=0 and
	// substring(r.strDate, 0, 6) =?2")
	// Set<String> findMouthByStrDate(String userId, String date);
	//
	// @Query("select count(*) from Remind where userId=?1 and repeatFlag=?2")
	// Long findMouthByRepeatFlagForWork(String userId, Integer repeatFlag);
	//
	// @Query("select count(*) from Remind where userId=?1 and repeatFlag=3 and repeatValue like
	// %?2%")
	// Long findMouthByWeek(String userId, String currWeek);
	/**
	 * 根据userId获取所有的提醒
	 * @param userId
	 * @param pageable
	 * @return
	 */
	@Query("from Remind where userId =?1 order by strDate desc")
	List<Remind> findByUserId(String userId);
}
