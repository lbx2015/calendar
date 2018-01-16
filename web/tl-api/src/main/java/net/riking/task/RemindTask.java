package net.riking.task;

import java.util.List;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.config.Const;
import net.riking.dao.ReportCompletedRelDao;
import net.riking.dao.repo.SysNoticeRepo;
import net.riking.entity.model.Jdpush;
import net.riking.entity.model.SysNotice;
import net.riking.entity.resp.CurrentReportTaskResp;
import net.riking.spring.SpringBeanUtil;
import net.riking.util.DateUtils;
import net.riking.util.JdpushUtil;

/***
 * 当天有核销任务的报表提醒
 * @author james.you
 * @version crateTime：2017年12月26日 下午6:12:13
 * @used TODO
 */
@Component("remindTask")
public class RemindTask extends TimerTask {

	@Autowired
	ReportCompletedRelDao reportCompletedRelDao;
	
	@Autowired
	private SysNoticeRepo sysNoticeRepo;

	@Override
	public void run() {
		reportCompletedRelDao = (ReportCompletedRelDao) SpringBeanUtil.getInstance().getBean("reportCompletedRelDaoImpl");
		String currentDate = DateUtils.getDate("yyyyMMdd");
		List<CurrentReportTaskResp> taskList = reportCompletedRelDao.findUsersByCurrentDayTasks(currentDate);
		
		if (taskList != null && taskList.size() > 0) {
			Jdpush jdpush = new Jdpush();
			jdpush.setNotificationTitle("您有相关的报表未核销哦。");
			jdpush.setMsgTitle("您有相关的报表未核销哦。");
			jdpush.setMsgContent("您有相关的报表未核销哦。");
			jdpush.getExtrasMap().put("dataType", Const.MQ_OPT_REPORT_TASK+"");
			for (CurrentReportTaskResp data : taskList) {
				if(StringUtils.isNotBlank(data.getPhoneDeviceId())){
					jdpush.setRegisrationId(data.getPhoneDeviceId());
					JdpushUtil.sendToRegistrationId(jdpush);
					
					//提醒信息保存
					SysNotice notice = new SysNotice();
					notice.setTitle(jdpush.getNotificationTitle());
					notice.setContent(jdpush.getMsgContent());
					notice.setDataType(Const.MQ_SYS_INFO);
					notice.setFromUserId(Const.SYS_NOTICE_FROME_SYS);
					notice.setNoticeUserId(data.getUserId());
					sysNoticeRepo.save(notice);
				}
			}
		}
	}

}
