package net.riking.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.dao.repo.AppUserRepo;
import net.riking.dao.repo.ReportSubmitCaliberRepo;
import net.riking.dao.repo.SysDaysRepo;
import net.riking.entity.model.Jdpush;
import net.riking.entity.model.Period;
import net.riking.entity.model.SysDays;
import net.riking.service.impl.SysDateServiceImpl;
import net.riking.util.JdpushUtil;

@Component("remindTask")
public class RemindTask extends TimerTask {

	@Autowired
	ReportSubmitCaliberRepo reportSubmitCaliberRepo;

	@Autowired
	SysDateServiceImpl sysDateService;

//	@Autowired
//	AppUserReportRelRepo appUserReportRelRepo;

	@Autowired
	AppUserRepo appUserRepo;

	@Autowired
	SysDaysRepo sysDaysRepo;
	
	
	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = new Date();
		String date = sdf.format(d);
		SysDays day = sysDaysRepo.findOne(date);
		Set<String> reportId = new HashSet<>();
		Period periods = sysDateService.getDate(date, "0");
		if (day.getIsWork() == 1) {
			Period period = sysDateService.getDate(date, "1");
			reportId = reportSubmitCaliberRepo.findByWorkDatefromReportId(period.getWeek(), period.getTen(),
					period.getMonth(), period.getSeason(), period.getHalfYear(), period.getYear());
			Set<String> reportIdAdd = reportSubmitCaliberRepo.findByFreeDatefromReportId(periods.getWeek(),
					periods.getTen(), periods.getMonth(), periods.getSeason(), periods.getHalfYear(),
					periods.getYear());
			for (String string : reportIdAdd) {
				reportId.add(string);
			}
		} else {
			reportId = reportSubmitCaliberRepo.findByFreeDatefromReportId(periods.getWeek(), periods.getTen(),
					periods.getMonth(), periods.getSeason(), periods.getHalfYear(), periods.getYear());
		}
		Set<String> userId = new HashSet<String>();
		// TODO 暫時注釋
		// appUserReportRelRepo.findbyReportId(reportId);
		Set<String> phoneSeqNum = new HashSet<String>();
		// TODO 暫時注釋
		// appUserRepo.findByUserId(userId);
		Jdpush jdpush = new Jdpush();
		if (phoneSeqNum.size() > 0) {
			jdpush.setNotificationTitle("您有相关的报表未报送！！！");
			jdpush.setMsgTitle("您有相关的报表未报送！！！");
			jdpush.setMsgContent("您有相关的报表未报送！！！");
			jdpush.setExtrasparam("这是空");
			for (String string : phoneSeqNum) {
				jdpush.setRegisrationId(string);
				JdpushUtil.sendToRegistrationId(jdpush);
			}
		}
	}

}
