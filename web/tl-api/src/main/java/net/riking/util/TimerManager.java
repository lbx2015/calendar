package net.riking.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.task.BirthdayTimerTask;
import net.riking.task.DeleteTempTimerTask;
import net.riking.task.RemindTask;

//@Component("timerManager")
@Component("timerManager")
public class TimerManager {
	protected final transient Logger logger = LogManager.getLogger(TimerManager.class);

	@Autowired
	BirthdayTimerTask birthdayTimerTask;
	@Autowired
	RemindTask remindTask;
	@Autowired
	DeleteTempTimerTask deleteTempTimerTask;

	// 每天重复时间间隔
	private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

	public void init() {
		logger.info("*************** TimerManager *********************");
		Timer timer = new Timer();
		birthdayTimerTask = new BirthdayTimerTask();
		remindTask = new RemindTask();
		deleteTempTimerTask = new DeleteTempTimerTask();
		
		/*** 定制每日10:00执行生日提醒方法 ***/
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 10);
		calendar.set(Calendar.MINUTE, 00);
		calendar.set(Calendar.SECOND, 00);
		timer.schedule(birthdayTimerTask, calendar.getTime(), PERIOD_DAY);

		/*** 定制每日9:00执行报表方法 ***/
		Calendar calendar2 = Calendar.getInstance();

		calendar2.set(Calendar.HOUR_OF_DAY, 9);
		calendar2.set(Calendar.MINUTE, 00);
		calendar2.set(Calendar.SECOND, 00);
		timer.schedule(remindTask, calendar2.getTime(), PERIOD_DAY);

		RemindTask remindTask2 = new RemindTask();
		/*** 定制每日16:59再次提醒报表方法 ***/
		Calendar calendar3 = Calendar.getInstance();
		calendar3.set(Calendar.HOUR_OF_DAY, 16);
		calendar3.set(Calendar.MINUTE, 59);
		calendar3.set(Calendar.SECOND, 59);
		timer.schedule(remindTask2, calendar3.getTime(), PERIOD_DAY);
		
		// *** 定制每日00:00执行删除临时文件方法 ***//
		Calendar calendar4 = Calendar.getInstance();
		calendar4.set(Calendar.HOUR_OF_DAY, 0);
		calendar4.set(Calendar.MINUTE, 0);
		calendar4.set(Calendar.SECOND, 0);
		Date date4 = calendar4.getTime();
		if (date4.before(new Date())) {
			date4 = this.addDay(date4, 1);
		}
		timer.schedule(deleteTempTimerTask, date4, PERIOD_DAY);
		
		logger.info("*************** TimerManager END*********************");
	}

	// 增加或减少天数
	private Date addDay(Date date, int num) {
		Calendar startDT = Calendar.getInstance();
		startDT.setTime(date);
		startDT.add(Calendar.DAY_OF_MONTH, num);
		return startDT.getTime();
	}

}
