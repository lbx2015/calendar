package net.riking.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import net.riking.entity.model.SysNoticeRead;
import net.riking.entity.model.SysNoticeResult;

public interface SysNoticeService {

	/**
	 * 根据用户查找该用户系统消息结果集
	 * @author james.you
	 * @version crateTime：2018年1月3日 下午3:50:41
	 * @used TODO
	 * @param userId
	 * @return
	 */
	public List<SysNoticeResult> findSysNoticeResult(String userId);
	
	/***
	 * 根据用户查找该用户通知消息结果集
	 * @author james.you
	 * @version crateTime：2018年1月4日 下午5:42:54
	 * @used TODO
	 * @param userId
	 * @param reqTimeStamp
	 * @return
	 */
	public List<SysNoticeResult> findUserNoticeResult(String userId, Date reqTimeStamp);

	/***
	 * 阅读系统消息
	 * @author james.you
	 * @version crateTime：2018年1月3日 下午4:08:46
	 * @used TODO
	 * @param data
	 */
	public void readSysNotice(SysNoticeRead data);
	
	/***
	 * 根据用户，删除选中的系统消息
	 * @author james.you
	 * @version crateTime：2018年1月3日 下午4:09:10
	 * @used TODO
	 * @param userId
	 * @param haveSysInfo false-没有；true-有
	 * @param noticeIds
	 */
	public void batchDelete(String userId, boolean haveSysInfo, Collection<String> noticeIds);
	
}
