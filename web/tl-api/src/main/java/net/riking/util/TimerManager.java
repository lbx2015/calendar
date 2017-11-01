package net.riking.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.riking.task.BirthdayTimerTask;
import net.riking.task.DelayDateTask;
import net.riking.task.RemindTask;
@Component("timerManager")
public class TimerManager {
	@Autowired
	BirthdayTimerTask birthdayTimerTask;
	@Autowired
	RemindTask remindTask;
	@Autowired
	DelayDateTask delayDateTask;
	//时间间隔
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    public void init() {
         Calendar calendar = Calendar.getInstance(); 
         Calendar calendar2 = Calendar.getInstance(); 
         Calendar calendar3 = Calendar.getInstance(); 
                
         /*** 定制每日10:00执行生日提醒方法 ***/

         calendar.set(Calendar.HOUR_OF_DAY,10 );
         calendar.set(Calendar.MINUTE, 00);
         calendar.set(Calendar.SECOND, 00);
         
         /*** 定制每日9:00执行报表方法 ***/

         calendar2.set(Calendar.HOUR_OF_DAY,9);
         calendar2.set(Calendar.MINUTE, 00);
         calendar2.set(Calendar.SECOND, 00);
         
         /*** 定制每日00:00执行报表方法 ***/

         calendar3.set(Calendar.HOUR_OF_DAY,17);
         calendar3.set(Calendar.MINUTE, 57);
         calendar3.set(Calendar.SECOND, 31);
 
          
         Date date=calendar.getTime(); //第一次执行定时任务的时间
         Date date2=calendar2.getTime();
         Date date3 = calendar3.getTime();
         System.err.println(date);
         System.err.println("before 方法比较："+date.before(new Date()));
         //如果第一次执行定时任务的时间 小于 当前的时间
         //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。循环执行的周期则以当前时间为准
         if (date.before(new Date())) {
             date = this.addDay(date, 1);
             System.out.println(date);
         }
         if (date2.before(new Date())) {
             date2 = this.addDay(date2, 1);
         }
         if (date3.before(new Date())) {
        	 date3 = this.addDay(date3, 1);
         }
          
         Timer timer = new Timer();

         //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
         timer.schedule(birthdayTimerTask,date,PERIOD_DAY);
         timer.schedule(remindTask, date2,PERIOD_DAY);
         timer.schedule(delayDateTask, date3,PERIOD_DAY);
        }

        // 增加或减少天数
        public Date addDay(Date date, int num) {
         Calendar startDT = Calendar.getInstance();
         startDT.setTime(date);
         startDT.add(Calendar.DAY_OF_MONTH, num);
         return startDT.getTime();
        }  
}
