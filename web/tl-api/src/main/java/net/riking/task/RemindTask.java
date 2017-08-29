package net.riking.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.entity.model.Days;
import net.riking.entity.model.Jdpush;
import net.riking.service.repo.AppUserRepo;
import net.riking.service.repo.AppUserReportRelRepo;
import net.riking.service.repo.DaysRepo;
import net.riking.service.repo.ReportSubmitCaliberRepo;
import net.riking.util.JdpushUtil;
@Component("remindTask")
public class RemindTask extends TimerTask {

	@Autowired
	ReportSubmitCaliberRepo reportSubmitCaliberRepo;
	@Autowired
	DaysRepo daysRepo;
	@Autowired
	AppUserReportRelRepo appUserReportRelRepo;
	@Autowired
	AppUserRepo appUserRepo;
	@Override
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = new Date();
		String date = sdf.format(d);
		Days day = daysRepo.findOne(date);
		Integer tenWorkDay;
		Integer tenAllDay;
		if (date.substring(6, 7).equals("0")) {
			tenWorkDay = daysRepo.findByDate(date.substring(0, 6) + "01", date);
			tenAllDay = Integer.valueOf(date) - Integer.valueOf(date.substring(0, 6) + "00");
		} else if (date.substring(6, 7).equals("1")) {
			tenWorkDay = daysRepo.findByDate(date.substring(0, 6) + "11", date);
			tenAllDay = Integer.valueOf(date) - Integer.valueOf(date.substring(0, 6) + "10");
		} else {
			tenWorkDay = daysRepo.findByDate(date.substring(0, 6) + "21", date);
			tenAllDay = Integer.valueOf(date) - Integer.valueOf(date.substring(0, 6) + "20");
		}
		Integer monthWorkDay = daysRepo.findByDate(date.substring(0, 6) + "01", date);
		Integer monthAllDay = Integer.valueOf(date) - Integer.valueOf(date.substring(0, 6) + "00");
		Integer seasonWorkDay;
		Integer seasonAllDay;
		if (3 < Integer.valueOf(date.substring(4, 6)) && Integer.valueOf(date.substring(4, 6)) < 7) {
			seasonWorkDay = daysRepo.findByDate(date.substring(0, 4) + "0401", date);
			seasonAllDay = daysRepo.findByAllDate(date.substring(0, 4) + "0401", date);
		} else if (6 < Integer.valueOf(date.substring(4, 6)) && Integer.valueOf(date.substring(4, 6)) < 10) {
			seasonWorkDay = daysRepo.findByDate(date.substring(0, 4) + "0701", date);
			seasonAllDay = daysRepo.findByAllDate(date.substring(0, 4) + "0701", date);
		} else if (Integer.valueOf(date.substring(4, 6)) > 9) {
			seasonWorkDay = daysRepo.findByDate(date.substring(0, 4) + "1001", date);
			seasonAllDay = daysRepo.findByAllDate(date.substring(0, 4) + "1001", date);
		} else {
			seasonWorkDay = daysRepo.findByDate(date.substring(0, 4) + "0101", date);
			seasonAllDay = daysRepo.findByAllDate(date.substring(0, 4) + "0101", date);
		}
		Integer halfYearWorkDay;
		Integer halfYearAllDay;
		if (Integer.valueOf(date.substring(4, 6)) < 7) {
			halfYearWorkDay = daysRepo.findByDate(date.substring(0, 4) + "0101", date);
			halfYearAllDay = daysRepo.findByAllDate(date.substring(0, 4) + "0101", date);
		} else {
			halfYearWorkDay = daysRepo.findByDate(date.substring(0, 4) + "0701", date);
			halfYearAllDay = daysRepo.findByAllDate(date.substring(0, 4) + "0701", date);
		}
		Integer yearWorkDay = daysRepo.findByDate(date.substring(0, 4) + "0101", date);
		Integer yearAllDay = daysRepo.findByAllDate(date.substring(0, 4) + "0101", date);
		Set<String> reportId = new HashSet<>();
		if (day.getIsWork()==1) {
			reportId = reportSubmitCaliberRepo.findByWorkDatefromReportId(Integer.valueOf(day.getWeekday()), tenWorkDay,
					monthWorkDay, seasonWorkDay, halfYearWorkDay, yearWorkDay);
			Set<String> reportIdAdd = reportSubmitCaliberRepo.findByFreeDatefromReportId(
					Integer.valueOf(day.getWeekday()), tenAllDay, monthAllDay, seasonAllDay, halfYearAllDay,
					yearAllDay);
			for (String string : reportIdAdd) {
				reportId.add(string);
			}
		} else {
			reportId = reportSubmitCaliberRepo.findByFreeDatefromReportId(Integer.valueOf(day.getWeekday()), tenAllDay,
					monthAllDay, seasonAllDay, halfYearAllDay, yearAllDay);
		}
		Set<String> userId = appUserReportRelRepo.findbyReportId(reportId);
		Set<String> phoneSeqNum = appUserRepo.findByUserId(userId);
		Jdpush jdpush = new Jdpush();
		if (phoneSeqNum.size()>0) {
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
