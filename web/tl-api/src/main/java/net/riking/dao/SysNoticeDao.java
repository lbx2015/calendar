package net.riking.dao;

import java.util.Date;
import java.util.List;

import net.riking.entity.model.SysNoticeResult;

public interface SysNoticeDao {

	/**
	 * 根据用户查找该用户的系统通知结果集
	 * @author james.you
	 * @version crateTime：2018年1月3日 下午3:50:41
	 * @used TODO
	 * @param userId
	 * @return
	 */
	public List<SysNoticeResult> findSysNoticeResult(String userId);
	
	/**
	 * 根据用户查找该用户非系统通知结果集
	 * @author james.you
	 * @version crateTime：2018年1月3日 下午3:50:41
	 * @used TODO
	 * @param userId
	 * @param reqTimeStamp
	 * @return
	 */
	public List<SysNoticeResult> findUserNoticeResult(String userId, Date reqTimeStamp);

	
}
