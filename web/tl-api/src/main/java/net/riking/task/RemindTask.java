package net.riking.task;

import java.util.List;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.dao.ReportCompletedRelDao;
import net.riking.entity.model.Jdpush;
import net.riking.entity.resp.CurrentReportTaskResp;
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

	@Override
	public void run() {
		String currentDate = DateUtils.getDate("yyyyMMdd");
		List<CurrentReportTaskResp> taskList = reportCompletedRelDao.findUsersByCurrentDayTasks(currentDate);
		
		if (taskList != null && taskList.size() > 0) {
			Jdpush jdpush = new Jdpush();
			jdpush.setNotificationTitle("您有相关的报表未核销哦。");
			jdpush.setMsgTitle("您有相关的报表未核销哦。");
			jdpush.setMsgContent("您有相关的报表未核销哦。");
			jdpush.setExtrasparam("");
			for (CurrentReportTaskResp data : taskList) {
				jdpush.setRegisrationId(data.getPhoneDeviceId());
				JdpushUtil.sendToRegistrationId(jdpush);
			}
		}
	}

}
