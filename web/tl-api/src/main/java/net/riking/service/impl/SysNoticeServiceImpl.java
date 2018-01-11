package net.riking.service.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.riking.config.Const;
import net.riking.dao.SysNoticeDao;
import net.riking.dao.repo.SysNoticeReadRepo;
import net.riking.dao.repo.SysNoticeRepo;
import net.riking.entity.model.SysNoticeRead;
import net.riking.entity.model.SysNoticeResult;
import net.riking.service.SysNoticeService;

/***
 * 消息系统服务接口
 * @author james.you
 * @version crateTime：2018年1月3日 下午4:03:08
 * @used TODO
 */
@Service("sysNoticeService")
public class SysNoticeServiceImpl implements SysNoticeService {
	Logger logger = LogManager.getLogger(getClass());

	@Autowired
	SysNoticeDao sysNoticeDao;
	
	@Autowired
	SysNoticeRepo sysNoticeRepo;
	
	@Autowired
	SysNoticeReadRepo sysNoticeReadRepo;
	

	@Override
	public List<SysNoticeResult> findSysNoticeResult(String userId) {
		// TODO Auto-generated method stub
		return sysNoticeDao.findSysNoticeResult(userId);
	}
	
	@Override
	public List<SysNoticeResult> findUserNoticeResult(String userId, Date reqTimeStamp) {
		// TODO Auto-generated method stub
		return sysNoticeDao.findUserNoticeResult(userId, reqTimeStamp);
	}

	@Override
	public void readSysNotice(SysNoticeRead data) {
		// TODO Auto-generated method stub
		SysNoticeRead entity = sysNoticeReadRepo.findByOne(data.getUserId(), data.getNoticeId());
		if(entity == null)
			sysNoticeReadRepo.save(data);
	}

	@Override
	@Transactional
	public void batchDelete(String userId, boolean haveSysInfo, Collection<String> noticeIds) {
		// TODO Auto-generated method stub
		
		if(haveSysInfo){
			//删除部分有系统类型的消息
			List<String> noticeIdList = sysNoticeRepo.findIdsBySysInfo(userId);
//			String[] arr_noticeId = new String[noticeIdList.size()];
//			noticeIdList.toArray(arr_noticeId);
			
			for(String noticeid : noticeIdList){
				SysNoticeRead entity = new SysNoticeRead();
				entity.setUserId(userId);
				entity.setNoticeId(noticeid);
				entity.setIsDeleted(Const.IS_DELETE);//删除
				sysNoticeReadRepo.save(entity);
			}
			sysNoticeRepo.batchDelete(noticeIdList);
		}
		if(null!=noticeIds && !noticeIds.isEmpty()){
			sysNoticeReadRepo.batchDelete(userId, noticeIds);
			sysNoticeRepo.batchDelete(noticeIds);
		}
	}

	

	
}
